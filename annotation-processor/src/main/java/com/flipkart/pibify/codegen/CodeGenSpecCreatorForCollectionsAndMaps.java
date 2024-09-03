package com.flipkart.pibify.codegen;

import static com.flipkart.pibify.codegen.CodeGenUtil.isCollectionOrMap;

/**
 * This class is used for creating mock CodeGenSpecs out of Field Types
 * Author bageshwar.pn
 * Date 24/08/24
 */
public class CodeGenSpecCreatorForCollectionsAndMaps {

    public CodeGenSpec create(CodeGenSpec.DataType dataType, String fieldName, Integer counter) throws CodeGenException {
        if (!isCollectionOrMap(dataType)) {
            throw new UnsupportedOperationException("CodeGenSpecCreatorForCollectionsAndMaps should be used only for maps and collections");
        }

        CodeGenSpec codeGenSpec = new CodeGenSpec("", fieldName + "Handler" + counter);
        codeGenSpec.addField(getFieldSpec(dataType));
        return codeGenSpec;
    }

    private CodeGenSpec.FieldSpec getFieldSpec(CodeGenSpec.DataType dataType) {
        CodeGenSpec.FieldSpec fieldSpec = new CodeGenSpec.FieldSpec();
        //String name = reflectedField.getName();
        fieldSpec.setIndex(1);
        //fieldSpec.setName(name);
//        fieldSpec.setType(getTypeFromJavaType(reflectedField.getName(), reflectedField.getGenericType(),
//                reflectedField.getType()));
//        fieldSpec.setGetter(namesToBeanInfo.get(name).getReadMethod().getName());
//        fieldSpec.setSetter(namesToBeanInfo.get(name).getWriteMethod().getName());

        return fieldSpec;
    }
}
