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

import com.flipkart.pibify.codegen.stub.PibifyGenerated;
import com.flipkart.pibify.generated.com.flipkart.pibify.test.data.ClassWithNativeFieldsHandler;
import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.util.SimpleCompiler;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unchecked")
class PibifyHandlerCacheGeneratorTest {

    private static Optional<PibifyGenerated<?>> invokeGeneratedCode(JavaFile javaFile, Class<?> target) throws Exception {

        SimpleCompiler.INSTANCE.compile(javaFile.toJavaFileObject());
        Class<?> handlerClazz = SimpleCompiler.INSTANCE.loadClass(javaFile.packageName + ".PibifyHandlerCache");
        Method getInstanceMethod = handlerClazz.getDeclaredMethod("getInstance");
        Object handlerInstance = getInstanceMethod.invoke(null);
        Method getHandlerMethod = handlerClazz.getMethod("getHandler", Class.class);
        return (Optional<PibifyGenerated<?>>) getHandlerMethod.invoke(handlerInstance, target);
    }

    @Test
    public void testBasic() throws Exception {

        PibifyHandlerCacheGenerator impl = new PibifyHandlerCacheGenerator("test");
        impl.add(ClassWithNativeFields.class, ClassName.get(ClassWithNativeFieldsHandler.class));
        JavaFile javaFile = impl.generateCacheClass().getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        Optional<PibifyGenerated<?>> pibifyGenerated = invokeGeneratedCode(javaFile, ClassWithNativeFields.class);
        assertNotNull(pibifyGenerated);
        assertTrue(pibifyGenerated.isPresent());
        assertEquals(ClassWithNativeFieldsHandler.class, pibifyGenerated.get().getClass());


        pibifyGenerated = invokeGeneratedCode(javaFile, null);
        assertNotNull(pibifyGenerated);
        assertFalse(pibifyGenerated.isPresent());

    }
}