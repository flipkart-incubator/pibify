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
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class is used for being the entry point to the code gen module
 * Author bageshwar.pn
 * Date 26/09/24
 *
 */
@Mojo(name = "generate",
        defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME
        //configurator = "include-project-dependencies"
)
public class PibifyGenerateSourcesMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources", required = true)
    private File outputDirectory;

    /**
     * The plugin descriptor
     *
     * @parameter default-value="${descriptor}"
     */
    @Parameter(defaultValue = "${descriptor}")
    private PluginDescriptor descriptor;

    @Parameter()
    private List<String> excludes;

    private ExecutorService fileWriterThreadPool;

    public PibifyGenerateSourcesMojo() {
        getLog().info("Initiated PibifyGenerateSourcesMojo ");
        this.fileWriterThreadPool = new ThreadPoolExecutor(20, 50,
                120L, TimeUnit.SECONDS,         // keep-alive time for idle threads
                new LinkedBlockingQueue<>(100000), // bounded queue with 100 capacity
                new ThreadPoolExecutor.CallerRunsPolicy()); // silently drop tasks if queue is full
    }

    @Override
    public void setLog(Log log) {
        super.setLog(new PrefixLog(log, "[Pibify] "));
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        // To be able to get module's dependencies be available to the plugin's classpath
        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();

        try {
            Thread.currentThread().setContextClassLoader(getClassLoader());
            // TODO fix the classloader issue
            /*List runtimeClasspathElements = project.getRuntimeClasspathElements();
            ClassRealm realm = descriptor.getClassRealm();

            for (Object element : runtimeClasspathElements)
            {
                File elementFile = new File(element.toString());
                realm.addURL(elementFile.toURI().toURL());
            }*/
            executeImpl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldCL);
        }
    }

    private void executeImpl() throws MojoExecutionException, MojoFailureException {
        if (!outputDirectory.exists()) {
            getLog().info("Creating directory " + outputDirectory.getAbsolutePath());
            boolean mkdir = outputDirectory.mkdir();
            if (!mkdir) {
                getLog().error("Unable to create directory " + outputDirectory.getAbsolutePath());
                throw new RuntimeException();
            }
        }

        SourcesScanner scanner = new JavaSourcesScanner(getLog());
        // TODO take third party processor config from pom
        ICodeGenSpecCreator codeGenSpecCreator = new BeanIntrospectorBasedCodeGenSpecCreator(getLog());
        // TODO consume config from pom
        PibifyConfiguration.builder().build();
        PibifyHandlerCacheGenerator handlerCacheGenerator = new PibifyHandlerCacheGenerator(project.getGroupId(), project.getArtifactId());
        ICodeGenerator codeGenerator = new CodeGeneratorImpl(handlerCacheGenerator.getClassName());
        Set<Class<?>> pibifyAnnotatedClasses = scanner.getPibifyAnnotatedClasses(project.getBuild().getOutputDirectory(), excludes);
        List<String> classesWithErrors = new ArrayList<>();
        List<String> skippedClasses = new ArrayList<>();

        int i = 0;
        for (Class<?> pibifyAnnotatedClass : pibifyAnnotatedClasses) {
            i++;
            try {
                String fileCount = i + "\\" + pibifyAnnotatedClasses.size() + "\t";
                getLog().debug("Processing file " + fileCount + pibifyAnnotatedClass.getName());
                System.out.print("\r" + fileCount);
                CodeGenSpec codeGenSpec = codeGenSpecCreator.create(pibifyAnnotatedClass);
                printCodeSpecGenLogs(codeGenSpecCreator);
                if (SpecGenLogLevel.INFO.equals(codeGenSpecCreator.status(pibifyAnnotatedClass))) {
                    if (generateHandler(pibifyAnnotatedClass)) {
                        JavaFileWrapper javaFile = codeGenerator.generate(codeGenSpec);
                        writeFile(javaFile);
                        handlerCacheGenerator.add(pibifyAnnotatedClass, javaFile.getClassName());
                    } else {
                        skippedClasses.add(pibifyAnnotatedClass.getName());
                        getLog().info("Skipping " + pibifyAnnotatedClass.getName());
                    }
                } else {
                    getLog().error("Unable to generate CodeGenSpec for " + pibifyAnnotatedClass.getName());
                    for (SpecGenLog specGenLog : codeGenSpecCreator.getLogsForCurrentEntity(pibifyAnnotatedClass)) {
                        getLog().error(" - " + specGenLog.getLogMessage());
                    }
                    classesWithErrors.add(pibifyAnnotatedClass.getName());
                }
            } catch (IOException e) {
                throw new RuntimeException("Fatal error while generating CodeGenSpec " + pibifyAnnotatedClass.getName(), e);
            } catch (CodeGenException e) {
                classesWithErrors.add(pibifyAnnotatedClass.getName());
                getLog().error("Exception while generating CodeGenSpec " + pibifyAnnotatedClass.getName(), e);
            }
        }

        getLog().info("*** Pibify Summary ***");
        getLog().info("\t #Processed\t" + (pibifyAnnotatedClasses.size()));
        getLog().info("\t #Success\t" + (pibifyAnnotatedClasses.size() - classesWithErrors.size()));
        getLog().info("\t #Failed\t" + (classesWithErrors.size()));
        getLog().info("\t #Skipped\t" + (skippedClasses.size()));
        String delimiter = "\n\t\t\t\t";
        getLog().warn("Classes with issues: " + delimiter + String.join(delimiter, classesWithErrors));
        getLog().warn("Skipped Classes: " + delimiter + String.join(delimiter, skippedClasses));

        try {
            handlerCacheGenerator.generateCacheClass().getJavaFile().writeTo(outputDirectory);
        } catch (IOException | CodeGenException e) {
            throw new RuntimeException(e);
        }

        if (!project.getCompileSourceRoots().contains(outputDirectory.getPath())) {
            project.addCompileSourceRoot(outputDirectory.getPath());
        }
    }

    private void writeFile(JavaFileWrapper javaFile) throws IOException {
        this.fileWriterThreadPool.submit(() -> {
            try {
                getLog().debug("Writing Java File " + javaFile.getPackageName().replaceAll("\\.", "/") + "/" + javaFile.getClassName() + ".java");
                javaFile.getJavaFile().writeTo(outputDirectory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private boolean generateHandler(Class<?> pibifyAnnotatedClass) {
        return !(pibifyAnnotatedClass.isInterface() ||
                Modifier.isAbstract(pibifyAnnotatedClass.getModifiers()));

    }

    private void printCodeSpecGenLogs(ICodeGenSpecCreator codeGenSpecCreator) {
        for (SpecGenLog specGenLog : codeGenSpecCreator.getLogsForCurrentEntity()) {
            switch (specGenLog.getLogLevel()) {
                case INFO:
                    getLog().debug(specGenLog.getLogMessage());
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

    private ClassLoader getClassLoader() throws MojoExecutionException {
        try {
            List<String> classpathElements = project.getRuntimeClasspathElements();
            URL urls[] = new URL[classpathElements.size()];

            for (int i = 0; i < urls.length; i++) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }
            return new URLClassLoader(urls, getClass().getClassLoader());
        } catch (Exception e) {
            throw new MojoExecutionException("Couldn't create a classloader.", e);
        }
    }
}
