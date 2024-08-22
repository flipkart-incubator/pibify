package com.flipkart.pibify.codegen;

import com.flipkart.pibify.test.data.ClassWithAutoboxFields;
import com.flipkart.pibify.test.data.ClassWithNativeArrays;
import com.flipkart.pibify.test.data.ClassWithNativeCollections;
import com.flipkart.pibify.test.data.ClassWithNativeCollectionsOfCollections;
import com.flipkart.pibify.test.data.ClassWithNativeCollectionsOfCollections2;
import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.data.ClassWithReferences;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class BeanIntrospectorBasedCodeGenSpecCreatorTest {

    @Test
    void createClassWithNativeFields() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeFields.class);
        assertNotNull(codeGenSpec);
        assertEquals(9, codeGenSpec.getFields().size());

        Map<String, CodeGenSpec.FieldSpec> nameToFields = codeGenSpec.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));

        CodeGenSpec.FieldSpec field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().nativeType);

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.INT, field.getType().nativeType);

        field = nameToFields.get("aLong");
        assertEquals("aLong", field.getName());
        assertEquals("getaLong", field.getGetter());
        assertEquals("setaLong", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.LONG, field.getType().nativeType);

        field = nameToFields.get("aFloat");
        assertEquals("aFloat", field.getName());
        assertEquals("getaFloat", field.getGetter());
        assertEquals("setaFloat", field.getSetter());
        assertEquals(4, field.getIndex());
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().nativeType);

        field = nameToFields.get("aDouble");
        assertEquals("aDouble", field.getName());
        assertEquals("getaDouble", field.getGetter());
        assertEquals("setaDouble", field.getSetter());
        assertEquals(5, field.getIndex());
        assertEquals(CodeGenSpec.DataType.DOUBLE, field.getType().nativeType);

        field = nameToFields.get("aDouble");
        assertEquals("aDouble", field.getName());
        assertEquals("getaDouble", field.getGetter());
        assertEquals("setaDouble", field.getSetter());
        assertEquals(5, field.getIndex());
        assertEquals(CodeGenSpec.DataType.DOUBLE, field.getType().nativeType);

        field = nameToFields.get("aBoolean");
        assertEquals("aBoolean", field.getName());
        assertEquals("isaBoolean", field.getGetter());
        assertEquals("setaBoolean", field.getSetter());
        assertEquals(6, field.getIndex());
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().nativeType);

        field = nameToFields.get("aChar");
        assertEquals("aChar", field.getName());
        assertEquals("getaChar", field.getGetter());
        assertEquals("setaChar", field.getSetter());
        assertEquals(7, field.getIndex());
        assertEquals(CodeGenSpec.DataType.CHAR, field.getType().nativeType);

        field = nameToFields.get("aByte");
        assertEquals("aByte", field.getName());
        assertEquals("getaByte", field.getGetter());
        assertEquals("setaByte", field.getSetter());
        assertEquals(8, field.getIndex());
        assertEquals(CodeGenSpec.DataType.BYTE, field.getType().nativeType);

        field = nameToFields.get("aShort");
        assertEquals("aShort", field.getName());
        assertEquals("getaShort", field.getGetter());
        assertEquals("setaShort", field.getSetter());
        assertEquals(9, field.getIndex());
        assertEquals(CodeGenSpec.DataType.SHORT, field.getType().nativeType);
    }

    @Test
    void createClassWithAutoboxFields() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithAutoboxFields.class);
        assertNotNull(codeGenSpec);
        assertEquals(8, codeGenSpec.getFields().size());

        Map<String, CodeGenSpec.FieldSpec> nameToFields = codeGenSpec.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));

        CodeGenSpec.FieldSpec field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.INT, field.getType().nativeType);

        field = nameToFields.get("aLong");
        assertEquals("aLong", field.getName());
        assertEquals("getaLong", field.getGetter());
        assertEquals("setaLong", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.LONG, field.getType().nativeType);

        field = nameToFields.get("aFloat");
        assertEquals("aFloat", field.getName());
        assertEquals("getaFloat", field.getGetter());
        assertEquals("setaFloat", field.getSetter());
        assertEquals(4, field.getIndex());
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().nativeType);

        field = nameToFields.get("aDouble");
        assertEquals("aDouble", field.getName());
        assertEquals("getaDouble", field.getGetter());
        assertEquals("setaDouble", field.getSetter());
        assertEquals(5, field.getIndex());
        assertEquals(CodeGenSpec.DataType.DOUBLE, field.getType().nativeType);

        field = nameToFields.get("aDouble");
        assertEquals("aDouble", field.getName());
        assertEquals("getaDouble", field.getGetter());
        assertEquals("setaDouble", field.getSetter());
        assertEquals(5, field.getIndex());
        assertEquals(CodeGenSpec.DataType.DOUBLE, field.getType().nativeType);

        field = nameToFields.get("aBoolean");
        assertEquals("aBoolean", field.getName());
        assertEquals("getaBoolean", field.getGetter());
        assertEquals("setaBoolean", field.getSetter());
        assertEquals(6, field.getIndex());
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().nativeType);

        field = nameToFields.get("aChar");
        assertEquals("aChar", field.getName());
        assertEquals("getaChar", field.getGetter());
        assertEquals("setaChar", field.getSetter());
        assertEquals(7, field.getIndex());
        assertEquals(CodeGenSpec.DataType.CHAR, field.getType().nativeType);

        field = nameToFields.get("aByte");
        assertEquals("aByte", field.getName());
        assertEquals("getaByte", field.getGetter());
        assertEquals("setaByte", field.getSetter());
        assertEquals(8, field.getIndex());
        assertEquals(CodeGenSpec.DataType.BYTE, field.getType().nativeType);

        field = nameToFields.get("aShort");
        assertEquals("aShort", field.getName());
        assertEquals("getaShort", field.getGetter());
        assertEquals("setaShort", field.getSetter());
        assertEquals(9, field.getIndex());
        assertEquals(CodeGenSpec.DataType.SHORT, field.getType().nativeType);
    }

    @Test
    void createClassWithNativeArrays() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeArrays.class);
        assertNotNull(codeGenSpec);
        assertEquals(3, codeGenSpec.getFields().size());

        Map<String, CodeGenSpec.FieldSpec> nameToFields = codeGenSpec.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));

        CodeGenSpec.FieldSpec field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.ARRAY, field.getType().nativeType);
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().containerTypes.get(0).nativeType);
        assertNull(field.getType().containerTypes.get(0).containerTypes);

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.ARRAY, field.getType().nativeType);
        assertEquals(CodeGenSpec.DataType.INT, field.getType().containerTypes.get(0).nativeType);
        assertNull(field.getType().containerTypes.get(0).containerTypes);

        field = nameToFields.get("aBoolean");
        assertEquals("aBoolean", field.getName());
        assertEquals("getaBoolean", field.getGetter());
        assertEquals("setaBoolean", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.ARRAY, field.getType().nativeType);
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().containerTypes.get(0).nativeType);
        assertNull(field.getType().containerTypes.get(0).containerTypes);

    }

    @Test
    void createClassWithNativeCollections() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeCollections.class);
        assertNotNull(codeGenSpec);
        assertEquals(3, codeGenSpec.getFields().size());

        Map<String, CodeGenSpec.FieldSpec> nameToFields = codeGenSpec.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));

        CodeGenSpec.FieldSpec field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().nativeType);
        assertEquals(CodeGenSpec.CollectionType.LIST, field.getType().collectionType);
        assertNotNull(field.getType().containerTypes);
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().containerTypes.get(0).nativeType);

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().nativeType);
        assertEquals(CodeGenSpec.CollectionType.SET, field.getType().collectionType);
        assertNotNull(field.getType().containerTypes);
        assertEquals(CodeGenSpec.DataType.INT, field.getType().containerTypes.get(0).nativeType);

        field = nameToFields.get("aMap");
        assertEquals("aMap", field.getName());
        assertEquals("getaMap", field.getGetter());
        assertEquals("setaMap", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.MAP, field.getType().nativeType);
        assertEquals(2, field.getType().containerTypes.size());
        assertNotNull(field.getType().containerTypes.get(0));
        assertNotNull(field.getType().containerTypes.get(1));
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().containerTypes.get(0).nativeType);
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().containerTypes.get(1).nativeType);

        /* Unknown collections not supported right now
        field = nameToFields.get("aCollection");
        assertEquals("aCollection", field.getName());
        assertEquals("getaCollection", field.getGetter());
        assertEquals("setaCollection", field.getSetter());
        assertEquals(4, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().nativeType);
        assertEquals(CodeGenSpec.CollectionType.LIST, field.getType().collectionType);
        assertNotNull(field.getType().containerTypes);
        assertEquals(CodeGenSpec.DataType.UNKNOWN, field.getType().containerTypes.get(0).nativeType);
        */

    }

    @Test
    void createClassWithReference() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithReferences.class);
        assertNotNull(codeGenSpec);
        assertEquals(2, codeGenSpec.getFields().size());

        Map<String, CodeGenSpec.FieldSpec> nameToFields = codeGenSpec.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));

        CodeGenSpec.FieldSpec field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().nativeType);

        field = nameToFields.get("reference");
        assertEquals("reference", field.getName());
        assertEquals("getReference", field.getGetter());
        assertEquals("setReference", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.OBJECT, field.getType().nativeType);
        assertNull(field.getType().containerTypes);
        assertNotNull(field.getType().referenceType);
        assertEquals("com.flipkart.pibify.test.data.another",
                field.getType().referenceType.getPackageName());

        assertEquals("AnotherClassWithNativeCollections",
                field.getType().referenceType.getClassName());

        // assert on ref class
        nameToFields = field.getType().referenceType.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));
        assertEquals(3, field.getType().referenceType.getFields().size());

        field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().nativeType);
        assertEquals(CodeGenSpec.CollectionType.LIST, field.getType().collectionType);
        assertNotNull(field.getType().containerTypes);
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().containerTypes.get(0).nativeType);

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().nativeType);
        assertEquals(CodeGenSpec.CollectionType.SET, field.getType().collectionType);
        assertNotNull(field.getType().containerTypes);
        assertEquals(CodeGenSpec.DataType.INT, field.getType().containerTypes.get(0).nativeType);

        field = nameToFields.get("aMap");
        assertEquals("aMap", field.getName());
        assertEquals("getaMap", field.getGetter());
        assertEquals("setaMap", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.MAP, field.getType().nativeType);
        assertEquals(2, field.getType().containerTypes.size());
        assertNotNull(field.getType().containerTypes.get(0));
        assertNotNull(field.getType().containerTypes.get(1));
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().containerTypes.get(0).nativeType);
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().containerTypes.get(1).nativeType);
    }

    @Test
    void createClassWithNativeCollectionsOfCollections() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeCollectionsOfCollections.class);
        assertNotNull(codeGenSpec);
        assertEquals(3, codeGenSpec.getFields().size());

        Map<String, CodeGenSpec.FieldSpec> nameToFields = codeGenSpec.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));

        CodeGenSpec.FieldSpec field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().nativeType);
        assertEquals(CodeGenSpec.CollectionType.LIST, field.getType().collectionType);
        assertNotNull(field.getType().containerTypes);
        CodeGenSpec.Type listType = field.getType().containerTypes.get(0);
        assertEquals(CodeGenSpec.DataType.COLLECTION, listType.nativeType);
        assertNotNull(listType.containerTypes);
        assertEquals(CodeGenSpec.CollectionType.LIST, listType.collectionType);
        assertEquals(CodeGenSpec.DataType.STRING, listType.containerTypes.get(0).nativeType);

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().nativeType);
        assertEquals(CodeGenSpec.CollectionType.SET, field.getType().collectionType);
        assertNotNull(field.getType().containerTypes);

        listType = field.getType().containerTypes.get(0);
        assertEquals(CodeGenSpec.DataType.COLLECTION, listType.nativeType);
        assertEquals(CodeGenSpec.CollectionType.SET, listType.collectionType);
        assertNotNull(listType.collectionType);
        assertEquals(CodeGenSpec.DataType.INT, listType.containerTypes.get(0).nativeType);

        field = nameToFields.get("aMap");
        assertEquals("aMap", field.getName());
        assertEquals("getaMap", field.getGetter());
        assertEquals("setaMap", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.MAP, field.getType().nativeType);
        assertEquals(2, field.getType().containerTypes.size());
        assertNotNull(field.getType().containerTypes.get(0));
        assertNotNull(field.getType().containerTypes.get(1));
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().containerTypes.get(0).nativeType);

        CodeGenSpec.Type valueType = field.getType().containerTypes.get(1);
        assertEquals(CodeGenSpec.DataType.MAP, valueType.nativeType);
        assertEquals(2, valueType.containerTypes.size());
        assertNotNull(valueType.containerTypes.get(0));
        assertNotNull(valueType.containerTypes.get(1));
        assertEquals(CodeGenSpec.DataType.STRING, valueType.containerTypes.get(0).nativeType);
        assertEquals(CodeGenSpec.DataType.BOOLEAN, valueType.containerTypes.get(1).nativeType);


    }

    @Test
    void createClassWithNativeCollectionsOfCollections2() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeCollectionsOfCollections2.class);
        assertNotNull(codeGenSpec);
        assertEquals(3, codeGenSpec.getFields().size());

        Map<String, CodeGenSpec.FieldSpec> nameToFields = codeGenSpec.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));

        CodeGenSpec.FieldSpec field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().nativeType);
        assertEquals(CodeGenSpec.CollectionType.LIST, field.getType().collectionType);
        assertNotNull(field.getType().containerTypes);
        CodeGenSpec.Type listType = field.getType().containerTypes.get(0);
        assertEquals(CodeGenSpec.DataType.COLLECTION, listType.nativeType);
        assertNotNull(listType.containerTypes);
        assertEquals(CodeGenSpec.CollectionType.SET, listType.collectionType);
        assertEquals(CodeGenSpec.DataType.STRING, listType.containerTypes.get(0).nativeType);

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().nativeType);
        assertEquals(CodeGenSpec.CollectionType.SET, field.getType().collectionType);
        assertNotNull(field.getType().containerTypes);

        listType = field.getType().containerTypes.get(0);
        assertEquals(CodeGenSpec.DataType.COLLECTION, listType.nativeType);
        assertEquals(CodeGenSpec.CollectionType.LIST, listType.collectionType);
        assertNotNull(listType.collectionType);
        assertEquals(CodeGenSpec.DataType.INT, listType.containerTypes.get(0).nativeType);

        field = nameToFields.get("aMap");
        assertEquals("aMap", field.getName());
        assertEquals("getaMap", field.getGetter());
        assertEquals("setaMap", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.MAP, field.getType().nativeType);
        assertEquals(2, field.getType().containerTypes.size());
        assertNotNull(field.getType().containerTypes.get(0));
        assertNotNull(field.getType().containerTypes.get(1));
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().containerTypes.get(0).nativeType);

        CodeGenSpec.Type valueType = field.getType().containerTypes.get(1);
        assertEquals(CodeGenSpec.DataType.COLLECTION, valueType.nativeType);
        assertEquals(1, valueType.containerTypes.size());
        assertEquals(CodeGenSpec.CollectionType.LIST, valueType.collectionType);

        valueType = valueType.containerTypes.get(0);
        assertEquals(CodeGenSpec.DataType.COLLECTION, valueType.nativeType);
        assertEquals(1, valueType.containerTypes.size());
        assertEquals(CodeGenSpec.CollectionType.SET, valueType.collectionType);

        valueType = valueType.containerTypes.get(0);
        assertEquals(CodeGenSpec.DataType.MAP, valueType.nativeType);
        assertNotNull(valueType.containerTypes.get(0));
        assertNotNull(valueType.containerTypes.get(1));
        assertEquals(CodeGenSpec.DataType.STRING, valueType.containerTypes.get(0).nativeType);
        assertEquals(CodeGenSpec.DataType.BOOLEAN, valueType.containerTypes.get(1).nativeType);


    }
}