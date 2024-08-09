package com.flipkart.pibify;


import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.flipkart.pibify.codegen.TagPredictor;
import com.flipkart.pibify.validation.InvalidPibifyAnnotation;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//
@State(Scope.Benchmark)
public class VanillaTest {

    final ClassWithNativeFields nativeFields = new ClassWithNativeFields();
    final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serializeClassWithNativeFieldsPibify() throws IOException {
        byte[] serialized = ClassWithNativeFieldsSerializer.serialize(nativeFields);
        assertNotNull(serialized);
    }

    @Test
    public void serializeClassWithNativeFieldsJson() throws IOException {
        String serialized = objectMapper.writeValueAsString(nativeFields);
        assertNotNull(serialized);
    }

    @Test
    public void getBeanProperties() throws IntrospectionException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(nativeFields.getClass());
        BeanDescription beanDescription = objectMapper.getSerializationConfig().introspect(javaType);
        assertEquals(9, beanDescription.findProperties().size());

        BeanInfo beanInfo = Introspector.getBeanInfo(nativeFields.getClass());
        assertNotNull(beanInfo);
    }

    @Test
    public void deserializeClassWithNativeFieldsPibify() throws IOException {
        byte[] serialized = ClassWithNativeFieldsSerializer.serialize(nativeFields);
        ClassWithNativeFields deserialized = ClassWithNativeFieldsDeserializer.deserialize(serialized);
        assertEquals(nativeFields, deserialized);
    }

    @Test
    public void testTags() {
        Arrays.stream(ClassWithNativeFields.class.getDeclaredFields()).forEach(f ->
                {
                    try {
                        System.out.println(f.getName() + ":" + TagPredictor.getTagBasedOnField(f));
                    } catch (InvalidPibifyAnnotation e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    //@Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(warmups = 1, value = 1)
    @Warmup(batchSize = -1, iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(batchSize = -1, iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureSerializeClassWithNativeFieldsPibify(Blackhole bh) throws IOException {
        serializeClassWithNativeFieldsPibify();
    }

    //@Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(warmups = 1, value = 1)
    @Warmup(batchSize = -1, iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(batchSize = -1, iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureSerializeClassWithNativeFieldsJson(Blackhole bh) throws IOException {
        serializeClassWithNativeFieldsJson();
    }
}
