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

package com.flipkart.pibify.codegen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;

/**
 * This class is used as a wrapper on top of java poet's JavaFile
 * Author bageshwar.pn
 * Date 11/08/24
 */
public class JavaFileWrapper {

    private JavaFile javaFile;
    private String packageName;
    private ClassName className;

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

    public ClassName getClassName() {
        return className;
    }

    public void setClassName(ClassName className) {
        this.className = className;
    }
}
