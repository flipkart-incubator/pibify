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

    private final String packageName;
    private final String className;
    private final List<FieldSpec> fieldSpecs;

    public CodeGenSpec(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
        fieldSpecs = new ArrayList<>();
    }

    public void addField(FieldSpec fieldSpec) {
        fieldSpecs.add(fieldSpec);
    }

    public List<FieldSpec> getFields() {
        return fieldSpecs;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodeGenSpec that = (CodeGenSpec) o;

        return packageName.equals(that.packageName);
    }

    @Override
    public int hashCode() {
        return packageName.hashCode();
    }

    public enum DataType {

        STRING("String", String.class, String.class),
        INT("Int", int.class, Integer.class),
        FLOAT("Float", float.class, Float.class),
        DOUBLE("Double", double.class, Double.class),
        CHAR("Char", char.class, Character.class),
        BOOLEAN("Bool", boolean.class, Boolean.class),
        LONG("Long", long.class, Long.class),
        SHORT("Short", short.class, Short.class),
        BYTE("Byte", byte.class, Byte.class),

        /* Containers */
        ARRAY(null, null, null),
        COLLECTION(null, null, null),
        MAP(null, null, null),
        OBJECT(null, null, null),

        UNKNOWN(null, null, null); /*These types will be serde'd using json */

        private final String readWriteMethodName;
        private final Class<?> clazz;
        private final Class<?> autoboxedClass;

        DataType(String readWriteMethodName, Class<?> clazz, Class<?> autoboxedClass) {
            this.readWriteMethodName = readWriteMethodName;
            this.clazz = clazz;
            this.autoboxedClass = autoboxedClass;
        }

        public String getReadWriteMethodName() {
            return readWriteMethodName;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public Class<?> getAutoboxedClass() {
            return autoboxedClass;
        }
    }

    public enum CollectionType {
        LIST, SET, QUEUE, DEQUE
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
        List<Type> containerTypes;
        CodeGenSpec referenceType;
        CollectionType collectionType;
    }
}
