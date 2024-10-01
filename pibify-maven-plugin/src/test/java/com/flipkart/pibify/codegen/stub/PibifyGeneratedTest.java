package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.TestUtils;
import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.core.PibifyConfiguration;
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
            public byte[] serialize(Object object) throws PibifyCodeExecException {
                return new byte[0];
            }

            @Override
            public Object deserialize(byte[] bytes) throws PibifyCodeExecException {
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
            public byte[] serialize(Object object) throws PibifyCodeExecException {
                return new byte[0];
            }

            @Override
            public Object deserialize(byte[] bytes) throws PibifyCodeExecException {
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