package com.flipkart.pibify.codegen;

import com.squareup.javapoet.JavaFile;

/**
 * This class is used as a wrapper on top of java poet's JavaFile
 * Author bageshwar.pn
 * Date 11/08/24
 */
public class JavaFileWrapper {

    private JavaFile javaFile;
    private String packageName;

    public JavaFile getJavaFile() {
        return javaFile;
    }

    public void setJavaFile(JavaFile javaFile) {
        this.javaFile = javaFile;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
