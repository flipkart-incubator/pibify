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

import com.flipkart.pibify.TestUtils;
import com.flipkart.pibify.core.PibifyConfiguration;
import com.flipkart.pibify.test.data.ClassWithSchemaChange1;
import com.flipkart.pibify.test.data.ClassWithSchemaChange2;
import com.flipkart.pibify.test.util.SimpleCompiler;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 24/09/24
 */
public class ConfigBasedCodeGeneratorImplTest {

    @BeforeEach
    public void resetConfig() {
        TestUtils.resetConfig();
    }

    @Test
    public void testClassWithSchemaChange() throws Exception {
        PibifyConfiguration.builder()
                .ignoreUnknownFields(false)
                .build();
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithSchemaChange1.class);

        ICodeGenerator impl = new CodeGeneratorImpl();
        JavaFile javaFile = impl.generate(codeGenSpec).getJavaFile();
        assertNotNull(javaFile);
        //javaFile.writeTo(System.out);
        ClassWithSchemaChange1 testPayload = new ClassWithSchemaChange1();
        testPayload.randomize();

        SimpleCompiler simpleCompiler = new SimpleCompiler();

        byte[] bytes = CodeGeneratorImplTest.serialize(simpleCompiler, javaFile, testPayload);

        codeGenSpec = creator.create(ClassWithSchemaChange2.class);
        javaFile = impl.generate(codeGenSpec).getJavaFile();
        //javaFile.writeTo(System.out);
        assertNotNull(javaFile);

        try {
            CodeGeneratorImplTest.deserialize(simpleCompiler, javaFile, new ClassWithSchemaChange2(), bytes);
            fail();
        } catch (InvocationTargetException e) {
            assertEquals("java.lang.UnsupportedOperationException: Unable to find tag in gen code: 18", e.getTargetException().getMessage());
        }
    }
}
