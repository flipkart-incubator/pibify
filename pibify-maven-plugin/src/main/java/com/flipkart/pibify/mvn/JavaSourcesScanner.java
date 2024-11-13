package com.flipkart.pibify.mvn;

import com.flipkart.pibify.codegen.CodeGenUtil;
import com.flipkart.pibify.mvn.interfaces.SourcesScanner;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashSet;
import java.util.Objects;
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
    public Set<Class<?>> getPibifyAnnotatedClasses(String targetClassesDirName) throws MojoExecutionException {
        File targetClassesDir = new File(targetClassesDirName);
        if (!targetClassesDir.exists()) {
            String err = "Target classes directory does not exist: " + targetClassesDir.getAbsolutePath();
            getLog().warn(err);
            throw new MojoExecutionException(err);
        }

        try {
            URL[] urls = new URL[]{targetClassesDir.toURI().toURL()};
            URLClassLoader classLoader = new URLClassLoader(urls, getClass().getClassLoader());

            Set<Class<?>> loadedClasses = scanAndLoadClasses(targetClassesDir, "", classLoader);
            getLog().info("Loaded " + loadedClasses.size() + " classes");
            return loadedClasses;

        } catch (Exception e) {
            throw new MojoExecutionException("Error scanning and loading classes", e);
        }
    }

    private Set<Class<?>> scanAndLoadClasses(File dir, String packageName, ClassLoader classLoader) {
        Set<Class<?>> classes = new LinkedHashSet<>();

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                classes.addAll(scanAndLoadClasses(file, packageName + file.getName() + ".", classLoader));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + file.getName().substring(0, file.getName().length() - 6);
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
