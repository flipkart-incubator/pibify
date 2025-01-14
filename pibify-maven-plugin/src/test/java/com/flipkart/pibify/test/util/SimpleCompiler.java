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

package com.flipkart.pibify.test.util;


import com.flipkart.pibify.codegen.stub.PibifyGenerated;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

// Source https://gitlab.com/jcsahnwaldt/simple-java-tools/-/blob/master/src/main/java/simple/tools/SimpleCompiler.java?ref_type=heads

class NamedJavaFileObject extends SimpleJavaFileObject {
    final String name;

    protected NamedJavaFileObject(String name, Kind kind) {
        super(URI.create(name.replace('.', '/') + kind.extension), kind);
        this.name = name;
    }
}

public class SimpleCompiler {

    // Opened this for testing
    public static final SimpleCompiler INSTANCE = new SimpleCompiler();

    private final Map<String, byte[]> classes = new HashMap<>();
    private final Map<String, List<JavaFileObject>> packages = new HashMap<>();
    private final ClassLoader loader = new ClassLoader() {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = classes.get(name);
            if (bytes == null) throw new ClassNotFoundException(name);
            return super.defineClass(name, bytes, 0, bytes.length);
        }
    };
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final JavaFileManager manager =
            new ForwardingJavaFileManager<JavaFileManager>(compiler.getStandardFileManager(null, null, null)) {
                @Override
                public JavaFileObject getJavaFileForOutput(Location loc, String name, Kind kind, FileObject obj) {
                    return outputFile(name, kind);
                }

                @Override
                public Iterable<JavaFileObject> list(Location loc, String pkg, Set<Kind> kinds, boolean rec) throws IOException {
                    List<JavaFileObject> files = packages.get(pkg);
                    if (files != null) return files;
                    else return super.list(loc, pkg, kinds, rec);
                }

                @Override
                public String inferBinaryName(Location loc, JavaFileObject file) {
                    if (file instanceof NamedJavaFileObject) return ((NamedJavaFileObject) file).name;
                    else return super.inferBinaryName(loc, file);
                }
            };

    public static JavaFileObject sourceFile(String name, String source) {
        return inputFile(name, Kind.SOURCE, source);
    }

    private static JavaFileObject inputFile(String name, Kind kind, String content) {
        return new NamedJavaFileObject(name, kind) {
            @Override
            public CharSequence getCharContent(boolean b) {
                return content;
            }
        };
    }

    private static JavaFileObject inputFile(String name, Kind kind, byte[] content) {
        return new NamedJavaFileObject(name, kind) {
            @Override
            public InputStream openInputStream() {
                return new ByteArrayInputStream(content);
            }
        };
    }

    private JavaFileObject outputFile(String name, Kind kind) {
        return new NamedJavaFileObject(name, kind) {
            @Override
            public OutputStream openOutputStream() {
                return outputStream(name);
            }
        };
    }

    private OutputStream outputStream(String name) {
        return new ByteArrayOutputStream() {
            @Override
            public void close() {
                storeClass(name, toByteArray());
            }
        };
    }

    private void storeClass(String name, byte[] bytes) {
        classes.put(name, bytes);
        JavaFileObject file = inputFile(name, Kind.CLASS, bytes);
        int dot = name.lastIndexOf('.');
        String pkg = dot == -1 ? "" : name.substring(0, dot);
        packages.computeIfAbsent(pkg, k -> new ArrayList<>()).add(file);
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {

            Class<?> aClass = loader.loadClass(name);
            if (PibifyGenerated.class.isAssignableFrom(aClass)) {
                try {
                    PibifyHandlerCacheForTest.addEntry((Class) (((ParameterizedType) aClass.getGenericSuperclass()).getActualTypeArguments()[0]),
                            (PibifyGenerated) aClass.getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            return aClass;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Class<?> compile(String name, String source) throws ClassNotFoundException {
        compile(sourceFile(name, source));
        return loadClass(name);
    }

    public void compile(JavaFileObject... files) {
        compile(Arrays.asList(files));
    }

    public void compile(List<JavaFileObject> files) {
        if (files.isEmpty()) throw new RuntimeException("No input files");
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        CompilationTask task = compiler.getTask(null, manager, collector, null, null, files);
        boolean success = task.call();
        check(success, collector);
    }

    private void check(boolean success, DiagnosticCollector<?> collector) {
        for (Diagnostic<?> diagnostic : collector.getDiagnostics()) {
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                throw new RuntimeException(diagnostic.getMessage(Locale.US) +
                        " at " + diagnostic.getLineNumber() + ":" + diagnostic.getColumnNumber());
            }
        }
        if (!success) throw new RuntimeException("Unknown error");
    }

    public void refresh() {
        classes.clear();
    }
}

