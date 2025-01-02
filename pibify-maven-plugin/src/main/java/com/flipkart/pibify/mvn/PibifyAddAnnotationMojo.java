package com.flipkart.pibify.mvn;

import com.flipkart.pibify.mvn.util.PrefixLog;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is used for adding the pibify annotation on the configured source
 * Author bageshwar.pn
 * Date 05/10/24
 */
@Mojo(name = "annotate")
public class PibifyAddAnnotationMojo extends AbstractMojo {

    private static final String PIBIFY_IMPORT = "com.flipkart.pibify.core.Pibify";
    private static final String PIBIFY_ANNOTATION = "Pibify";
    private static final String JSON_IGNORE = "JsonIgnore";
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter()
    private List<String> excludes;

    public PibifyAddAnnotationMojo() {
        getLog().info("Initiated PibifyAddAnnotationMojo ");
    }

    public static void main(String[] args) {

        PibifyAddAnnotationMojo mojo = new PibifyAddAnnotationMojo();
        if (args.length != 1) {
            mojo.getLog().error("Usage: java PibifyAnnotator <directory>");
            System.exit(1);
        }

        String directoryPath = args[0];
        mojo.processDirectory(directoryPath);
    }

    @Override
    public void setLog(Log log) {
        super.setLog(new PrefixLog(log, "[Pibify] "));
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        processDirectory(project.getCompileSourceRoots().get(0));
    }

    private void processDirectory(String directoryPath) {
        getLog().debug("Starting to process directory: " + directoryPath);

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(directoryPath);
        if (excludes != null) {
            String[] excludePatterns = excludes.toArray(new String[0]);
            scanner.setExcludes(excludePatterns);
        }

        scanner.addDefaultExcludes();
        scanner.scan();

        getLog().info("Excluded files: " + Arrays.asList(scanner.getExcludedFiles()));

        for (String path : scanner.getIncludedFiles()) {
            processFile(Paths.get(directoryPath, path));
        }
        getLog().info("Finished processing directory: " + directoryPath);
    }

    private void processFile(Path filePath) {
        getLog().debug("Processing file: " + filePath);
        try {
            CompilationUnit cu = StaticJavaParser.parse(filePath);

            // Skip interfaces
            if (cu.getTypes().size() == 1) {
                TypeDeclaration<?> type = cu.getType(0);
                if (type instanceof ClassOrInterfaceDeclaration) {
                    if (((ClassOrInterfaceDeclaration) type).isInterface()) {
                        getLog().debug("Skipping interface " + filePath);
                        return;
                    }

                }
            }


            AtomicInteger counter = new AtomicInteger(1);
            boolean importAdded = addPibifyImport(cu);
            boolean updated = processFields(cu, counter);
            updated = updated | processEnumConstants(cu, counter);

            if (updated) {
                Files.write(filePath, cu.toString().getBytes());
                getLog().info("File updated: " + filePath);
            } else {
                getLog().debug("No changes made to file: " + filePath);
            }
        } catch (FileNotFoundException e) {
            getLog().error("File not found: " + filePath, e);
        } catch (IOException e) {
            getLog().error("Error writing to file: " + filePath, e);
        }
    }

    private boolean addPibifyImport(CompilationUnit cu) {
        if (cu.getImports().stream().noneMatch(i -> i.getNameAsString().equals(PIBIFY_IMPORT))) {
            cu.addImport(new ImportDeclaration(PIBIFY_IMPORT, false, false));
            getLog().debug("Added Pibify import");
            return true;
        }
        getLog().debug("Pibify import already exists");
        return false;
    }

    private boolean processFields(CompilationUnit cu, AtomicInteger counter) {
        boolean updated = false;
        for (FieldDeclaration f : cu.findAll(FieldDeclaration.class)) {
            if (!f.isStatic()) {
                updated = updated | addPibifyAnnotation(f, counter);
            }
        }

        return updated;
    }

    private boolean processEnumConstants(CompilationUnit cu, AtomicInteger counter) {
        boolean updated = false;
        for (EnumConstantDeclaration enumConstant : cu.findAll(EnumConstantDeclaration.class)) {
            updated = updated | addPibifyAnnotation(enumConstant, counter);
        }
        return updated;
    }

    private boolean addPibifyAnnotation(FieldDeclaration field, AtomicInteger counter) {
        if (field.getVariables().size() != 1) {
            getLog().error("Multiline fields found: " + field.getVariables().toString());
            throw new RuntimeException(field.getVariables().toString() + " - Multi-variable declarations are not supported. Split them into multiple lines");
        }

        String fieldName = field.getVariables().get(0).toString();

        boolean reindex = System.getProperty("reindex") != null;
        // This flag forces a complete re-index of all fields.
        // This is useful when onboarding pibify to a repo, which has a lot of churn and new fields have been added
        if (reindex) {
            field.getAnnotationByName(PIBIFY_ANNOTATION).ifPresent(Node::remove);
        }

        getLog().debug("Processing field: " + fieldName);
        if (field.getAnnotationByName(PIBIFY_ANNOTATION).isPresent() ||
                field.getAnnotationByName(JSON_IGNORE).isPresent()) {
            getLog().info("Skipping field: " + fieldName);
            counter.incrementAndGet();
            return false;
        } else {
            // TODO Support deprecated fields by understanding the last seen index
            field.addAndGetAnnotation(PIBIFY_ANNOTATION)
                    .addPair("value", new IntegerLiteralExpr(String.valueOf(counter.getAndIncrement())));
            getLog().debug("Added @Pibify annotation to field: " + fieldName);
            return true;
        }
    }

    private boolean addPibifyAnnotation(EnumConstantDeclaration enumConstant, AtomicInteger counter) {
        getLog().debug("Processing enum constant: " + enumConstant);
        if (enumConstant.getAnnotationByName(PIBIFY_ANNOTATION).isPresent()) {
            counter.incrementAndGet();
            return false;
        } else {
            // TODO Support deprecated fields by understanding the last seen index
            enumConstant.addAndGetAnnotation(PIBIFY_ANNOTATION)
                    .addPair("value", new IntegerLiteralExpr(String.valueOf(counter.getAndIncrement())));
            getLog().debug("Added @Pibify annotation to enum constant: " + enumConstant);
            return true;
        }
    }
}
