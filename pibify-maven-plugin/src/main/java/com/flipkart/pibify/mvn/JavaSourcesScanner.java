package com.flipkart.pibify.mvn;

import com.flipkart.pibify.codegen.CodeGenUtil;
import com.flipkart.pibify.mvn.interfaces.SourcesScanner;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used for scanning the configured directory for java classes and create a
 * list of classes who need to be pibified
 * Author bageshwar.pn
 * Date 28/09/24
 */
public class JavaSourcesScanner extends SourcesScanner {

    public JavaSourcesScanner(Log log) {
        super(log);
    }

    @Override
    public Set<Class<?>> getPibifyAnnotatedClasses(String targetClassesDirName, List<String> excludes) throws MojoExecutionException {
        File targetClassesDir = new File(targetClassesDirName);
        if (!targetClassesDir.exists()) {
            String err = "Target classes directory does not exist: " + targetClassesDir.getAbsolutePath();
            getLog().warn(err);
            throw new MojoExecutionException(err);
        }

        try {
            URL[] urls = new URL[]{targetClassesDir.toURI().toURL()};
            URLClassLoader classLoader = new URLClassLoader(urls, getClass().getClassLoader());

            DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(targetClassesDirName);

            if (excludes != null) {
                String[] excludePatterns = excludes.toArray(new String[0]);
                scanner.setExcludes(excludePatterns);
            }

            scanner.addDefaultExcludes();
            scanner.scan();

            getLog().info("Excluded files: " + Arrays.asList(scanner.getExcludedFiles()));

            Set<Class<?>> loadedClasses = scanAndLoadClasses(targetClassesDirName, scanner.getIncludedFiles(), classLoader);
            getLog().info("Loaded " + loadedClasses.size() + " classes");
            return loadedClasses;

        } catch (Exception e) {
            throw new MojoExecutionException("Error scanning and loading classes", e);
        }
    }

    private Set<Class<?>> scanAndLoadClasses(String baseDir, String[] files, ClassLoader classLoader) {
        Set<Class<?>> classes = new LinkedHashSet<>();

        for (String fileName : files) {
            if (fileName.endsWith(".class")) {
                // strip off the .class at the end of the filename and convert slash to dots for classloader to load the class
                String className = fileName.substring(0, fileName.length() - 6).replaceAll("/", ".");
                try {
                    Class<?> aClass = classLoader.loadClass(className);
                    if (!CodeGenUtil.isNonPibifyClass(aClass)) {
                        classes.add(aClass);
                        getLog().debug("  - " + aClass.getName());
                    } else {
                        getLog().debug("Ignoring non-pibify class " + aClass.getName());
                    }
                } catch (Exception e) {
                    getLog().error("Failed to load class: " + className, e);
                }
            }
        }

        return classes;
    }
}
