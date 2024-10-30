package com.flipkart.pibify.mvn;

import com.flipkart.pibify.codegen.BeanIntrospectorBasedCodeGenSpecCreator;
import com.flipkart.pibify.codegen.CodeGenException;
import com.flipkart.pibify.codegen.CodeGenSpec;
import com.flipkart.pibify.codegen.CodeGeneratorImpl;
import com.flipkart.pibify.codegen.ICodeGenSpecCreator;
import com.flipkart.pibify.codegen.ICodeGenerator;
import com.flipkart.pibify.codegen.JavaFileWrapper;
import com.flipkart.pibify.codegen.PibifyHandlerCacheGenerator;
import com.flipkart.pibify.codegen.log.SpecGenLog;
import com.flipkart.pibify.codegen.log.SpecGenLogLevel;
import com.flipkart.pibify.core.PibifyConfiguration;
import com.flipkart.pibify.mvn.interfaces.SourcesScanner;
import com.flipkart.pibify.mvn.util.PrefixLog;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * This class is used for being the entry point to the code gen module
 * Author bageshwar.pn
 * Date 26/09/24
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class PibifyGenerateSourcesMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/pibify", required = true)
    private File outputDirectory;

    public PibifyGenerateSourcesMojo() {
        super.setLog(new PrefixLog(new SystemStreamLog(), "[Pibify] "));
        getLog().info("Initiated PibifyGenerateSourcesMojo ");
    }

    @Override
    public void setLog(Log log) {
        //no-op
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!outputDirectory.exists()) {
            getLog().info("Creating directory " + outputDirectory.getAbsolutePath());
            boolean mkdir = outputDirectory.mkdir();
            if (!mkdir) {
                getLog().error("Unable to create directory " + outputDirectory.getAbsolutePath());
                throw new RuntimeException();
            }
        }

        SourcesScanner scanner = new JavaSourcesScanner(getLog());
        ICodeGenSpecCreator codeGenSpecCreator = new BeanIntrospectorBasedCodeGenSpecCreator();
        // TODO consume config from pom
        PibifyConfiguration.builder().build();
        ICodeGenerator codeGenerator = new CodeGeneratorImpl();
        PibifyHandlerCacheGenerator handlerCacheGenerator = new PibifyHandlerCacheGenerator(project.getGroupId(), project.getArtifactId());

        Set<Class<?>> pibifyAnnotatedClasses = scanner.getPibifyAnnotatedClasses(project.getBuild().getOutputDirectory());


        for (Class<?> pibifyAnnotatedClass : pibifyAnnotatedClasses) {
            try {
                getLog().debug("Processing file " + pibifyAnnotatedClass.getName());
                CodeGenSpec codeGenSpec = codeGenSpecCreator.create(pibifyAnnotatedClass);
                printCodeSpecGenLogs(codeGenSpecCreator);
                if (SpecGenLogLevel.INFO.equals(codeGenSpecCreator.status(pibifyAnnotatedClass))) {
                    JavaFileWrapper javaFile = codeGenerator.generate(codeGenSpec);
                    getLog().info("Writing Java File " + javaFile.getPackageName().replaceAll("\\.", "/") + "/" + codeGenSpec.getClassName() + ".java");
                    javaFile.getJavaFile().writeTo(outputDirectory);
                    handlerCacheGenerator.add(pibifyAnnotatedClass, javaFile.getClassName());
                } else {
                    getLog().error("Unable to generate CodeGenSpec for " + pibifyAnnotatedClass.getName());
                }
            } catch (IOException | CodeGenException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            handlerCacheGenerator.generateCacheClass().getJavaFile().writeTo(outputDirectory);
        } catch (IOException | CodeGenException e) {
            throw new RuntimeException(e);
        }

        if (!project.getCompileSourceRoots().contains(outputDirectory.getPath())) {
            project.addCompileSourceRoot(outputDirectory.getPath());
        }
    }

    private void printCodeSpecGenLogs(ICodeGenSpecCreator codeGenSpecCreator) {
        for (SpecGenLog specGenLog : codeGenSpecCreator.getLogsForCurrentEntity()) {
            switch (specGenLog.getLogLevel()) {
                case INFO:
                    getLog().info(specGenLog.getLogMessage());
                    break;
                case WARN:
                    getLog().warn(specGenLog.getLogMessage());
                    break;
                case ERROR:
                    getLog().error(specGenLog.getLogMessage());
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown log level " + specGenLog.getLogLevel());
            }
        }
    }
}
