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

package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.TestUtils;
import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.core.PibifyConfiguration;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.test.data.EnumB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PibifyGeneratedTest {

    @BeforeEach
    public void resetConfig() {
        TestUtils.resetConfig();
    }
    @Test
    public void getEnumValue() throws PibifyCodeExecException {
        PibifyConfiguration.builder()
                .build();
        PibifyGenerated<?> underTest = new PibifyGenerated<Object>() {
            @Override
            public void serialize(Object object, ISerializer serializer, SerializationContext context) throws PibifyCodeExecException {
            }

            @Override
            public Object deserialize(IDeserializer deserializer, Class clazz, SerializationContext context) throws PibifyCodeExecException {
                return null;
            }
        };

        assertEquals(EnumB.A, underTest.getEnumValue(EnumB.values(), 0));
        assertEquals(EnumB.B, underTest.getEnumValue(EnumB.values(), 1));
        assertEquals(EnumB.C, underTest.getEnumValue(EnumB.values(), 2));
        assertNull(underTest.getEnumValue(EnumB.values(), -1));
        assertNull(underTest.getEnumValue(EnumB.values(), 3));
        assertNull(underTest.getEnumValue(EnumB.values(), 4));
    }

    @Test
    public void getEnumValueWithException() throws PibifyCodeExecException {
        PibifyConfiguration.builder()
                .ignoreUnknownEnums(false)
                .build();
        PibifyGenerated<?> underTest = new PibifyGenerated<Object>() {
            @Override
            public void serialize(Object object, ISerializer serializer, SerializationContext context) throws PibifyCodeExecException {
            }

            @Override
            public Object deserialize(IDeserializer deserializer, Class clazz, SerializationContext context) throws PibifyCodeExecException {
                return null;
            }
        };

        assertEquals(EnumB.A, underTest.getEnumValue(EnumB.values(), 0));
        assertEquals(EnumB.B, underTest.getEnumValue(EnumB.values(), 1));
        assertEquals(EnumB.C, underTest.getEnumValue(EnumB.values(), 2));

        try {
            underTest.getEnumValue(EnumB.values(), -1);
        } catch (PibifyCodeExecException e) {
            assertEquals("Unknown Enum Cardinal -1 for com.flipkart.pibify.test.data.EnumB", e.getMessage());
        }

        try {
            underTest.getEnumValue(EnumB.values(), 3);
        } catch (PibifyCodeExecException e) {
            assertEquals("Unknown Enum Cardinal 3 for com.flipkart.pibify.test.data.EnumB", e.getMessage());
        }

        try {
            underTest.getEnumValue(EnumB.values(), 4);
        } catch (PibifyCodeExecException e) {
            assertEquals("Unknown Enum Cardinal 4 for com.flipkart.pibify.test.data.EnumB", e.getMessage());
        }
    }
}