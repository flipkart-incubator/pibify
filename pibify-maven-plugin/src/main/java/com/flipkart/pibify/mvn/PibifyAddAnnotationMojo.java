/*
 *
 *  *Copyright [2025] [Original Author]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.flipkart.pibify.mvn;

import com.flipkart.pibify.mvn.interfaces.IncrementalBuildProvider;
import com.flipkart.pibify.mvn.util.PrefixLog;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
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
import java.util.Optional;
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

    @Parameter(defaultValue = "false")
    private boolean incremental;

    @Parameter()
    private String incrementalBuildProvider;

    public PibifyAddAnnotationMojo() {
        getLog().info("Initiated PibifyAddAnnotationMojo ");
    }

    public static void main(String[] args) {
        PibifyAddAnnotationMojo mojo = new PibifyAddAnnotationMojo();
        if (args.length != 1) {
            mojo.getLog().error("Usage: java PibifyAddAnnotationMojo <directory>");
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
        if (incremental) {
            getLog().info("Annotating incrementally");
            try {
                if (incrementalBuildProvider == null || incrementalBuildProvider.isEmpty()) {
                    throw new MojoExecutionException("Incremental Build Provider is not set");
                }
                IncrementalBuildProvider provider = (IncrementalBuildProvider) Class.forName(incrementalBuildProvider).getDeclaredConstructor().newInstance();
                List<Path> changedFiles = provider.changedFiles(getLog(), Paths.get(project.getCompileSourceRoots().get(0)));
                for (Path changedFile : changedFiles) {
                    getLog().info("Annotating " + changedFile.toString());
                    processFile(changedFile, incremental);
                }
            } catch (Exception e) {
                getLog().error("Error in incremental build", e);
            }
        } else {
            processDirectory(project.getCompileSourceRoots().get(0));
        }
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
            processFile(Paths.get(directoryPath, path), incremental);
        }
        getLog().info("Finished processing directory: " + directoryPath);
    }

    private void processFile(Path filePath, boolean incremental) {
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
            AtomicInteger counter;
            if (incremental) {
                counter = new AtomicInteger(getMaxPibifyIndex(cu));
            } else {
                counter = new AtomicInteger(1);
            }
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

    private int getMaxPibifyIndex(CompilationUnit cu) {
        int max = 1;
        for (FieldDeclaration field : cu.findAll(FieldDeclaration.class)) {
            if (!field.isStatic()) {
                if (field.getAnnotationByName(PIBIFY_ANNOTATION).isPresent()) {
                    AnnotationExpr expr = field.getAnnotationByName(PIBIFY_ANNOTATION).get().asAnnotationExpr();
                    int idx = 0;
                    if (expr.isSingleMemberAnnotationExpr()) {
                        // is of the form `@Pibify(1)`
                        idx = expr.asSingleMemberAnnotationExpr().getMemberValue().asIntegerLiteralExpr().asNumber().intValue();
                    } else if (expr.isNormalAnnotationExpr()) {
                        // is of the form `@Pibify(value = 1)`
                        Optional<Integer> value = expr.asNormalAnnotationExpr().getPairs().stream().filter(f -> f.getNameAsString().equals("value")).map(f -> f.getValue().asIntegerLiteralExpr().asNumber().intValue()).findFirst();
                        if (value.isPresent()) {
                            idx = value.get();
                        } else {
                            throw new RuntimeException("Missing value key in Pibify Annotation");
                        }
                    } else {
                        // is of an unknown form
                        throw new RuntimeException("Unknown Expressions in Pibify Annotation");
                    }
                    if (idx > max) {
                        max = idx;
                    }
                }
            }
        }
        return max + 1;
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
        if (field.getAnnotationByName(PIBIFY_ANNOTATION).isPresent() || field.getAnnotationByName(JSON_IGNORE).isPresent()) {
            getLog().info("Skipping field: " + fieldName);
            if (!incremental) {
                // don't skip count in case of incremental builds, else it leads to double counting.
                counter.incrementAndGet();
            }
            return false;
        } else {
            // TODO Support deprecated fields by understanding the last seen index
            field.addAndGetAnnotation(PIBIFY_ANNOTATION).addPair("value", new IntegerLiteralExpr(String.valueOf(counter.getAndIncrement())));
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
            enumConstant.addAndGetAnnotation(PIBIFY_ANNOTATION).addPair("value", new IntegerLiteralExpr(String.valueOf(counter.getAndIncrement())));
            getLog().debug("Added @Pibify annotation to enum constant: " + enumConstant);
            return true;
        }
    }
}
