package com.flipkart.pibify.codegen;

import com.flipkart.pibify.codegen.log.FieldSpecGenLog;
import com.flipkart.pibify.codegen.log.SpecGenLog;
import com.flipkart.pibify.codegen.log.SpecGenLogLevel;
import com.flipkart.pibify.test.data.ClassHierarchy1;
import com.flipkart.pibify.test.data.ClassWithAutoboxFields;
import com.flipkart.pibify.test.data.ClassWithDuplicateFieldNames;
import com.flipkart.pibify.test.data.ClassWithInterestingFieldNames;
import com.flipkart.pibify.test.data.ClassWithInvalidPibifyIndex;
import com.flipkart.pibify.test.data.ClassWithNativeArrays;
import com.flipkart.pibify.test.data.ClassWithNativeCollections;
import com.flipkart.pibify.test.data.ClassWithNativeCollectionsOfCollections;
import com.flipkart.pibify.test.data.ClassWithNativeCollectionsOfCollections2;
import com.flipkart.pibify.test.data.ClassWithNativeFields;
import com.flipkart.pibify.test.data.ClassWithReferences;
import com.flipkart.pibify.test.data.jsoncreator.DuplicatePropertyNames;
import com.flipkart.pibify.test.data.jsoncreator.MismatchedPropertyNames;
import com.flipkart.pibify.test.data.jsoncreator.PartialConstructor;
import com.flipkart.pibify.test.data.jsoncreator.PartialConstructorWithNonPibify;
import com.flipkart.pibify.test.data.jsoncreator.PartialConstructorWithSetters;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanIntrospectorBasedCodeGenSpecCreatorTest {

    @Test
    public void createClassWithNativeFields() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeFields.class);
        assertNotNull(codeGenSpec);
        assertEquals(12, codeGenSpec.getFields().size());

        Map<String, CodeGenSpec.FieldSpec> nameToFields = codeGenSpec.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));

        CodeGenSpec.FieldSpec field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().getNativeType());

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.INT, field.getType().getNativeType());

        field = nameToFields.get("aLong");
        assertEquals("aLong", field.getName());
        assertEquals("getaLong", field.getGetter());
        assertEquals("setaLong", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.LONG, field.getType().getNativeType());

        field = nameToFields.get("aFloat");
        assertEquals("aFloat", field.getName());
        assertEquals("getaFloat", field.getGetter());
        assertEquals("setaFloat", field.getSetter());
        assertEquals(4, field.getIndex());
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().getNativeType());

        field = nameToFields.get("aDouble");
        assertEquals("aDouble", field.getName());
        assertEquals("getaDouble", field.getGetter());
        assertEquals("setaDouble", field.getSetter());
        assertEquals(5, field.getIndex());
        assertEquals(CodeGenSpec.DataType.DOUBLE, field.getType().getNativeType());

        field = nameToFields.get("aDouble");
        assertEquals("aDouble", field.getName());
        assertEquals("getaDouble", field.getGetter());
        assertEquals("setaDouble", field.getSetter());
        assertEquals(5, field.getIndex());
        assertEquals(CodeGenSpec.DataType.DOUBLE, field.getType().getNativeType());

        field = nameToFields.get("aBoolean");
        assertEquals("aBoolean", field.getName());
        assertEquals("isaBoolean", field.getGetter());
        assertEquals("setaBoolean", field.getSetter());
        assertEquals(6, field.getIndex());
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().getNativeType());

        field = nameToFields.get("aChar");
        assertEquals("aChar", field.getName());
        assertEquals("getaChar", field.getGetter());
        assertEquals("setaChar", field.getSetter());
        assertEquals(7, field.getIndex());
        assertEquals(CodeGenSpec.DataType.CHAR, field.getType().getNativeType());

        field = nameToFields.get("aByte");
        assertEquals("aByte", field.getName());
        assertEquals("getaByte", field.getGetter());
        assertEquals("setaByte", field.getSetter());
        assertEquals(8, field.getIndex());
        assertEquals(CodeGenSpec.DataType.BYTE, field.getType().getNativeType());

        field = nameToFields.get("aShort");
        assertEquals("aShort", field.getName());
        assertEquals("getaShort", field.getGetter());
        assertEquals("setaShort", field.getSetter());
        assertEquals(9, field.getIndex());
        assertEquals(CodeGenSpec.DataType.SHORT, field.getType().getNativeType());
    }

    @Test
    public void createClassWithAutoboxFields() throws CodeGenException {
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
        assertEquals(CodeGenSpec.DataType.INT, field.getType().getNativeType());

        field = nameToFields.get("aLong");
        assertEquals("aLong", field.getName());
        assertEquals("getaLong", field.getGetter());
        assertEquals("setaLong", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.LONG, field.getType().getNativeType());

        field = nameToFields.get("aFloat");
        assertEquals("aFloat", field.getName());
        assertEquals("getaFloat", field.getGetter());
        assertEquals("setaFloat", field.getSetter());
        assertEquals(4, field.getIndex());
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().getNativeType());

        field = nameToFields.get("aDouble");
        assertEquals("aDouble", field.getName());
        assertEquals("getaDouble", field.getGetter());
        assertEquals("setaDouble", field.getSetter());
        assertEquals(5, field.getIndex());
        assertEquals(CodeGenSpec.DataType.DOUBLE, field.getType().getNativeType());

        field = nameToFields.get("aDouble");
        assertEquals("aDouble", field.getName());
        assertEquals("getaDouble", field.getGetter());
        assertEquals("setaDouble", field.getSetter());
        assertEquals(5, field.getIndex());
        assertEquals(CodeGenSpec.DataType.DOUBLE, field.getType().getNativeType());

        field = nameToFields.get("aBoolean");
        assertEquals("aBoolean", field.getName());
        assertEquals("getaBoolean", field.getGetter());
        assertEquals("setaBoolean", field.getSetter());
        assertEquals(6, field.getIndex());
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().getNativeType());

        field = nameToFields.get("aChar");
        assertEquals("aChar", field.getName());
        assertEquals("getaChar", field.getGetter());
        assertEquals("setaChar", field.getSetter());
        assertEquals(7, field.getIndex());
        assertEquals(CodeGenSpec.DataType.CHAR, field.getType().getNativeType());

        field = nameToFields.get("aByte");
        assertEquals("aByte", field.getName());
        assertEquals("getaByte", field.getGetter());
        assertEquals("setaByte", field.getSetter());
        assertEquals(8, field.getIndex());
        assertEquals(CodeGenSpec.DataType.BYTE, field.getType().getNativeType());

        field = nameToFields.get("aShort");
        assertEquals("aShort", field.getName());
        assertEquals("getaShort", field.getGetter());
        assertEquals("setaShort", field.getSetter());
        assertEquals(9, field.getIndex());
        assertEquals(CodeGenSpec.DataType.SHORT, field.getType().getNativeType());
    }

    @Test
    public void createClassWithNativeArrays() throws CodeGenException {
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
        assertEquals(CodeGenSpec.DataType.ARRAY, field.getType().getNativeType());
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().getContainerTypes().get(0).getNativeType());
        assertNull(field.getType().getContainerTypes().get(0).getContainerTypes());

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.ARRAY, field.getType().getNativeType());
        assertEquals(CodeGenSpec.DataType.INT, field.getType().getContainerTypes().get(0).getNativeType());
        assertNull(field.getType().getContainerTypes().get(0).getContainerTypes());

        field = nameToFields.get("aBoolean");
        assertEquals("aBoolean", field.getName());
        assertEquals("getaBoolean", field.getGetter());
        assertEquals("setaBoolean", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.ARRAY, field.getType().getNativeType());
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().getContainerTypes().get(0).getNativeType());
        assertNull(field.getType().getContainerTypes().get(0).getContainerTypes());

    }

    @Test
    public void createClassWithNativeCollections() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithNativeCollections.class);
        assertNotNull(codeGenSpec);
        assertEquals(6, codeGenSpec.getFields().size());

        Map<String, CodeGenSpec.FieldSpec> nameToFields = codeGenSpec.getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));

        CodeGenSpec.FieldSpec field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().getNativeType());
        assertEquals(CodeGenSpec.CollectionType.LIST, field.getType().getCollectionType());
        assertNotNull(field.getType().getContainerTypes());
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().getContainerTypes().get(0).getNativeType());

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().getNativeType());
        assertEquals(CodeGenSpec.CollectionType.SET, field.getType().getCollectionType());
        assertNotNull(field.getType().getContainerTypes());
        assertEquals(CodeGenSpec.DataType.INT, field.getType().getContainerTypes().get(0).getNativeType());

        field = nameToFields.get("aMap");
        assertEquals("aMap", field.getName());
        assertEquals("getaMap", field.getGetter());
        assertEquals("setaMap", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.MAP, field.getType().getNativeType());
        assertEquals(2, field.getType().getContainerTypes().size());
        assertNotNull(field.getType().getContainerTypes().get(0));
        assertNotNull(field.getType().getContainerTypes().get(1));
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().getContainerTypes().get(0).getNativeType());
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().getContainerTypes().get(1).getNativeType());

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
    public void createClassWithReference() throws CodeGenException {
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
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().getNativeType());

        field = nameToFields.get("reference");
        assertEquals("reference", field.getName());
        assertEquals("getReference", field.getGetter());
        assertEquals("setReference", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.OBJECT, field.getType().getNativeType());
        assertNull(field.getType().getContainerTypes());
        assertNotNull(field.getType().getReferenceType());
        assertEquals("com.flipkart.pibify.test.data.another",
                field.getType().getReferenceType().getPackageName());

        assertEquals("AnotherClassWithNativeCollections",
                field.getType().getReferenceType().getClassName());

        // assert on ref class
        nameToFields = field.getType().getReferenceType().getFields().stream()
                .collect(Collectors.toMap(CodeGenSpec.FieldSpec::getName, f -> f));
        assertEquals(3, field.getType().getReferenceType().getFields().size());

        field = nameToFields.get("aString");
        assertEquals("aString", field.getName());
        assertEquals("getaString", field.getGetter());
        assertEquals("setaString", field.getSetter());
        assertEquals(1, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().getNativeType());
        assertEquals(CodeGenSpec.CollectionType.LIST, field.getType().getCollectionType());
        assertNotNull(field.getType().getContainerTypes());
        assertEquals(CodeGenSpec.DataType.STRING, field.getType().getContainerTypes().get(0).getNativeType());

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().getNativeType());
        assertEquals(CodeGenSpec.CollectionType.SET, field.getType().getCollectionType());
        assertNotNull(field.getType().getContainerTypes());
        assertEquals(CodeGenSpec.DataType.INT, field.getType().getContainerTypes().get(0).getNativeType());

        field = nameToFields.get("aMap");
        assertEquals("aMap", field.getName());
        assertEquals("getaMap", field.getGetter());
        assertEquals("setaMap", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.MAP, field.getType().getNativeType());
        assertEquals(2, field.getType().getContainerTypes().size());
        assertNotNull(field.getType().getContainerTypes().get(0));
        assertNotNull(field.getType().getContainerTypes().get(1));
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().getContainerTypes().get(0).getNativeType());
        assertEquals(CodeGenSpec.DataType.BOOLEAN, field.getType().getContainerTypes().get(1).getNativeType());
    }

    @Test
    public void createClassWithNativeCollectionsOfCollections() throws CodeGenException {
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
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().getNativeType());
        assertEquals(CodeGenSpec.CollectionType.LIST, field.getType().getCollectionType());
        assertNotNull(field.getType().getContainerTypes());
        CodeGenSpec.Type listType = field.getType().getContainerTypes().get(0);
        assertEquals(CodeGenSpec.DataType.COLLECTION, listType.getNativeType());
        assertNotNull(listType.getContainerTypes());
        assertEquals(CodeGenSpec.CollectionType.LIST, listType.getCollectionType());
        assertEquals(CodeGenSpec.DataType.STRING, listType.getContainerTypes().get(0).getNativeType());

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().getNativeType());
        assertEquals(CodeGenSpec.CollectionType.SET, field.getType().getCollectionType());
        assertNotNull(field.getType().getContainerTypes());

        listType = field.getType().getContainerTypes().get(0);
        assertEquals(CodeGenSpec.DataType.COLLECTION, listType.getNativeType());
        assertEquals(CodeGenSpec.CollectionType.SET, listType.getCollectionType());
        assertNotNull(listType.getCollectionType());
        assertEquals(CodeGenSpec.DataType.INT, listType.getContainerTypes().get(0).getNativeType());

        field = nameToFields.get("aMap");
        assertEquals("aMap", field.getName());
        assertEquals("getaMap", field.getGetter());
        assertEquals("setaMap", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.MAP, field.getType().getNativeType());
        assertEquals(2, field.getType().getContainerTypes().size());
        assertNotNull(field.getType().getContainerTypes().get(0));
        assertNotNull(field.getType().getContainerTypes().get(1));
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().getContainerTypes().get(0).getNativeType());

        CodeGenSpec.Type valueType = field.getType().getContainerTypes().get(1);
        assertEquals(CodeGenSpec.DataType.MAP, valueType.getNativeType());
        assertEquals(2, valueType.getContainerTypes().size());
        assertNotNull(valueType.getContainerTypes().get(0));
        assertNotNull(valueType.getContainerTypes().get(1));
        assertEquals(CodeGenSpec.DataType.STRING, valueType.getContainerTypes().get(0).getNativeType());
        assertEquals(CodeGenSpec.DataType.BOOLEAN, valueType.getContainerTypes().get(1).getNativeType());


    }

    @Test
    public void createClassWithNativeCollectionsOfCollections2() throws CodeGenException {
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
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().getNativeType());
        assertEquals(CodeGenSpec.CollectionType.LIST, field.getType().getCollectionType());
        assertNotNull(field.getType().getContainerTypes());
        CodeGenSpec.Type listType = field.getType().getContainerTypes().get(0);
        assertEquals(CodeGenSpec.DataType.COLLECTION, listType.getNativeType());
        assertNotNull(listType.getContainerTypes());
        assertEquals(CodeGenSpec.CollectionType.SET, listType.getCollectionType());
        assertEquals(CodeGenSpec.DataType.STRING, listType.getContainerTypes().get(0).getNativeType());

        field = nameToFields.get("anInt");
        assertEquals("anInt", field.getName());
        assertEquals("getAnInt", field.getGetter());
        assertEquals("setAnInt", field.getSetter());
        assertEquals(2, field.getIndex());
        assertEquals(CodeGenSpec.DataType.COLLECTION, field.getType().getNativeType());
        assertEquals(CodeGenSpec.CollectionType.SET, field.getType().getCollectionType());
        assertNotNull(field.getType().getContainerTypes());

        listType = field.getType().getContainerTypes().get(0);
        assertEquals(CodeGenSpec.DataType.COLLECTION, listType.getNativeType());
        assertEquals(CodeGenSpec.CollectionType.LIST, listType.getCollectionType());
        assertNotNull(listType.getCollectionType());
        assertEquals(CodeGenSpec.DataType.INT, listType.getContainerTypes().get(0).getNativeType());

        field = nameToFields.get("aMap");
        assertEquals("aMap", field.getName());
        assertEquals("getaMap", field.getGetter());
        assertEquals("setaMap", field.getSetter());
        assertEquals(3, field.getIndex());
        assertEquals(CodeGenSpec.DataType.MAP, field.getType().getNativeType());
        assertEquals(2, field.getType().getContainerTypes().size());
        assertNotNull(field.getType().getContainerTypes().get(0));
        assertNotNull(field.getType().getContainerTypes().get(1));
        assertEquals(CodeGenSpec.DataType.FLOAT, field.getType().getContainerTypes().get(0).getNativeType());

        CodeGenSpec.Type valueType = field.getType().getContainerTypes().get(1);
        assertEquals(CodeGenSpec.DataType.COLLECTION, valueType.getNativeType());
        assertEquals(1, valueType.getContainerTypes().size());
        assertEquals(CodeGenSpec.CollectionType.LIST, valueType.getCollectionType());

        valueType = valueType.getContainerTypes().get(0);
        assertEquals(CodeGenSpec.DataType.COLLECTION, valueType.getNativeType());
        assertEquals(1, valueType.getContainerTypes().size());
        assertEquals(CodeGenSpec.CollectionType.SET, valueType.getCollectionType());

        valueType = valueType.getContainerTypes().get(0);
        assertEquals(CodeGenSpec.DataType.MAP, valueType.getNativeType());
        assertNotNull(valueType.getContainerTypes().get(0));
        assertNotNull(valueType.getContainerTypes().get(1));
        assertEquals(CodeGenSpec.DataType.STRING, valueType.getContainerTypes().get(0).getNativeType());
        assertEquals(CodeGenSpec.DataType.BOOLEAN, valueType.getContainerTypes().get(1).getNativeType());
    }

    @Test
    public void testClassWithInvalidPibifyIndex() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithInvalidPibifyIndex.class);
        assertNotNull(codeGenSpec);
        assertNotNull(creator.getLogsForCurrentEntity());
        assertEquals(SpecGenLogLevel.ERROR, creator.status(ClassWithInvalidPibifyIndex.class));
        assertEquals(0, codeGenSpec.getFields().size());
        assertEquals(9, creator.getLogsForCurrentEntity().size());

        ArrayList<SpecGenLog> specGenLogs = new ArrayList<>(creator.getLogsForCurrentEntity());

        int index = 0;
        assertEquals("com.flipkart.pibify.test.data.ClassWithInvalidPibifyIndex BeanInfo missing for a", specGenLogs.get(index).getLogMessage());
        assertEquals(SpecGenLogLevel.ERROR, specGenLogs.get(index).getLogLevel());
        assertEquals("a", ((FieldSpecGenLog) (specGenLogs.get(index))).getFieldName());

        index++;
        assertEquals("com.flipkart.pibify.test.data.ClassWithInvalidPibifyIndex Field with duplicate index: a for b", specGenLogs.get(index).getLogMessage());
        assertEquals(SpecGenLogLevel.ERROR, specGenLogs.get(index).getLogLevel());
        assertEquals("b", ((FieldSpecGenLog) (specGenLogs.get(index))).getFieldName());

        index++;
        assertEquals("com.flipkart.pibify.test.data.ClassWithInvalidPibifyIndex BeanInfo missing for b", specGenLogs.get(index).getLogMessage());
        assertEquals(SpecGenLogLevel.ERROR, specGenLogs.get(index).getLogLevel());
        assertEquals("b", ((FieldSpecGenLog) (specGenLogs.get(index))).getFieldName());

        index++;
        assertEquals("com.flipkart.pibify.test.data.ClassWithInvalidPibifyIndex Index cannot be less than or equal to 0 for c", specGenLogs.get(index).getLogMessage());
        assertEquals(SpecGenLogLevel.ERROR, specGenLogs.get(index).getLogLevel());
        assertEquals("c", ((FieldSpecGenLog) (specGenLogs.get(index))).getFieldName());

        index++;
        assertEquals("com.flipkart.pibify.test.data.ClassWithInvalidPibifyIndex BeanInfo missing for c", specGenLogs.get(index).getLogMessage());
        assertEquals(SpecGenLogLevel.ERROR, specGenLogs.get(index).getLogLevel());
        assertEquals("c", ((FieldSpecGenLog) (specGenLogs.get(index))).getFieldName());

        index++;
        assertEquals("com.flipkart.pibify.test.data.ClassWithInvalidPibifyIndex Index cannot be more than 128 for d", specGenLogs.get(index).getLogMessage());
        assertEquals(SpecGenLogLevel.ERROR, specGenLogs.get(index).getLogLevel());
        assertEquals("d", ((FieldSpecGenLog) (specGenLogs.get(index))).getFieldName());

        index++;
        assertEquals("com.flipkart.pibify.test.data.ClassWithInvalidPibifyIndex BeanInfo missing for d", specGenLogs.get(index).getLogMessage());
        assertEquals(SpecGenLogLevel.ERROR, specGenLogs.get(index).getLogLevel());
        assertEquals("d", ((FieldSpecGenLog) (specGenLogs.get(index))).getFieldName());

        index++;
        assertEquals("com.flipkart.pibify.test.data.ClassWithInvalidPibifyIndex Index cannot be less than or equal to 0 for e", specGenLogs.get(index).getLogMessage());
        assertEquals(SpecGenLogLevel.ERROR, specGenLogs.get(index).getLogLevel());
        assertEquals("e", ((FieldSpecGenLog) (specGenLogs.get(index))).getFieldName());

        index++;
        assertEquals("com.flipkart.pibify.test.data.ClassWithInvalidPibifyIndex BeanInfo missing for e", specGenLogs.get(index).getLogMessage());
        assertEquals(SpecGenLogLevel.ERROR, specGenLogs.get(index).getLogLevel());
        assertEquals("e", ((FieldSpecGenLog) (specGenLogs.get(index))).getFieldName());

    }

    @Test
    public void testClassWithDuplicateFieldNames() throws CodeGenException {

        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithDuplicateFieldNames.class);
        assertNotNull(codeGenSpec);
        assertNotNull(creator.getLogsForCurrentEntity());
        assertEquals(SpecGenLogLevel.ERROR, creator.status(ClassWithDuplicateFieldNames.class));
        assertEquals(1, codeGenSpec.getFields().size());
        assertEquals(1, creator.getLogsForCurrentEntity().size());
        ArrayList<SpecGenLog> specGenLogs = new ArrayList<>(creator.getLogsForCurrentEntity());

        assertTrue(specGenLogs.get(0).getLogMessage().contains(
                "com.flipkart.pibify.test.data.ClassWithDuplicateFieldNames Duplicate field"
        ));
        assertEquals(SpecGenLogLevel.ERROR, specGenLogs.get(0).getLogLevel());
    }

    @Test
    public void testClassWithInterestingFieldNames() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassWithInterestingFieldNames.class);
        assertNotNull(codeGenSpec);
        assertNotNull(creator.getLogsForCurrentEntity());
        assertEquals(SpecGenLogLevel.INFO, creator.status(ClassWithInterestingFieldNames.class));
        assertEquals(5, codeGenSpec.getFields().size());
    }

    @Test
    public void testAbstractClass() throws CodeGenException {
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(ClassHierarchy1.class);
        assertNotNull(codeGenSpec);
        assertTrue(codeGenSpec.isAbstract());
    }

    @Test
    public void testPartialConstructor() throws CodeGenException {
        Class<?> forTest = PartialConstructor.class;
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(forTest);
        assertNotNull(codeGenSpec);
        assertEquals(SpecGenLogLevel.ERROR, creator.status(forTest));
        List<String> msgs = creator.getLogsForCurrentEntity().stream().map(SpecGenLog::getLogMessage).collect(Collectors.toList());

        assertEquals(2, creator.getLogsForCurrentEntity().size());
        assertEquals("com.flipkart.pibify.test.data.jsoncreator.PartialConstructor All fields must be present in the AllArgsConstructor", msgs.get(0));
        assertEquals("com.flipkart.pibify.test.data.jsoncreator.PartialConstructor Mismatch in Field aString2 in AllArgsConstructor", msgs.get(1));
    }

    @Test
    public void testPartialConstructorWithNonPibify() throws CodeGenException {
        Class<?> forTest = PartialConstructorWithNonPibify.class;
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(forTest);
        assertNotNull(codeGenSpec);
        assertEquals(SpecGenLogLevel.INFO, creator.status(forTest));
        assertEquals(0, creator.getLogsForCurrentEntity().size());
    }

    @Test
    public void testPartialConstructorWithSetters() throws CodeGenException {
        Class<?> forTest = PartialConstructorWithSetters.class;
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(forTest);
        assertNotNull(codeGenSpec);
        assertEquals(SpecGenLogLevel.ERROR, creator.status(forTest));
        List<String> msgs = creator.getLogsForCurrentEntity().stream().map(SpecGenLog::getLogMessage).collect(Collectors.toList());
        assertEquals(2, creator.getLogsForCurrentEntity().size());
        assertEquals("com.flipkart.pibify.test.data.jsoncreator.PartialConstructorWithSetters All fields must be present in the AllArgsConstructor", msgs.get(0));
        assertEquals("com.flipkart.pibify.test.data.jsoncreator.PartialConstructorWithSetters Mismatch in Field aString2 in AllArgsConstructor", msgs.get(1));
    }

    @Test
    public void testMismatchedPropertyNames() throws CodeGenException {
        Class<?> forTest = MismatchedPropertyNames.class;
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(forTest);
        assertNotNull(codeGenSpec);
        assertEquals(SpecGenLogLevel.ERROR, creator.status(forTest));
        List<String> msgs = creator.getLogsForCurrentEntity().stream().map(SpecGenLog::getLogMessage).collect(Collectors.toList());
        assertEquals(2, msgs.size());
        assertEquals("com.flipkart.pibify.test.data.jsoncreator.MismatchedPropertyNames Additional Field aString1 in AllArgsConstructor", msgs.get(0));
        assertEquals("com.flipkart.pibify.test.data.jsoncreator.MismatchedPropertyNames Mismatch in Field aString in AllArgsConstructor", msgs.get(1));
    }

    @Test
    public void testDuplicatePropertyNames() throws CodeGenException {
        Class<?> forTest = DuplicatePropertyNames.class;
        BeanIntrospectorBasedCodeGenSpecCreator creator = new BeanIntrospectorBasedCodeGenSpecCreator();
        CodeGenSpec codeGenSpec = creator.create(forTest);
        assertNotNull(codeGenSpec);
        assertEquals(SpecGenLogLevel.ERROR, creator.status(forTest));
        assertEquals(1, creator.getLogsForCurrentEntity().size());
        assertEquals("com.flipkart.pibify.test.data.jsoncreator.DuplicatePropertyNames Mismatch in Field aString1 in AllArgsConstructor",
                creator.getLogsForCurrentEntity().stream().findFirst().get().getLogMessage());
    }
}