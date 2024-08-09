package com.flipkart.pibify.codegen;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the input that will go to the code gen.
 * <p>
 * It has to contain the fields and their types that will be used for generating serde steps.
 * <p>
 * The input to
 * Author bageshwar.pn
 * Date 09/08/24
 */
public class CodeGenSpec {

    /*
    This is not marked generic because adding a type parameter to the class is purely syntactic sugar.
    Instead, the type of the class will be referenced via a member variable.
     */

    private final String fqdn;
    private final String className;
    private final List<FieldSpec> fieldSpecs;

    public CodeGenSpec(String fqdn, String className) {
        this.fqdn = fqdn;
        this.className = className;
        fieldSpecs = new ArrayList<>();
    }

    public void addField(FieldSpec fieldSpec) {
        fieldSpecs.add(fieldSpec);
    }

    public List<FieldSpec> getFields() {
        return fieldSpecs;
    }

    public String getFqdn() {
        return fqdn;
    }

    public String getClassName() {
        return className;
    }

    public enum DataType {
        /**
         * aString = "";
         * anInt = -1;
         * aFloat = -1;
         * aDouble = -1;
         * aChar = '0';
         * aBoolean = false;
         * aLong = -1;
         * aShort = -1;
         * aByte = -1;
         */

        STRING,
        INT,
        FLOAT,
        DOUBLE,
        CHAR,
        BOOLEAN,
        LONG,
        SHORT,
        BYTE,

        /* Containers */
        ARRAY,
        COLLECTION,
        MAP,
        OBJECT,

        UNKNOWN /*These types will be serde'd using json */
    }

    public enum ContainerType {
        ARRAY,
        COLLECTION,
        OBJECT
    }

    public static class FieldSpec {
        String name;
        int index;
        Type type;
        String getter;
        String setter;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getGetter() {
            return getter;
        }

        public void setGetter(String getter) {
            this.getter = getter;
        }

        public String getSetter() {
            return setter;
        }

        public void setSetter(String setter) {
            this.setter = setter;
        }
    }

    public static class Type {
        DataType nativeType;
        Type containerType;
        List<Type> followingContainerTypes;
    }
}
