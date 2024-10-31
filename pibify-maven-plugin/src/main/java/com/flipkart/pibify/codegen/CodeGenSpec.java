package com.flipkart.pibify.codegen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

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
    private final boolean isInnerClass;
    private boolean isAbstract = false;

    private final ClassName jpClassName;

    private final Map<String, Object> thirdPartyData;

    public CodeGenSpec(String packageName, String className, boolean isInnerClass) {
        // to ensure the package name in imports does not have $.
        // in case of inner classes, the enclosing class's package becomes a problem
        this.packageName = packageName.replaceAll("\\$", ".");
        this.className = className;
        this.jpClassName = ClassName.get(packageName, className);
        this.isInnerClass = isInnerClass;
        fieldSpecs = new ArrayList<>();
        thirdPartyData = new HashMap<>();
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

    public boolean isInnerClass() {
        return isInnerClass;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public ClassName getJpClassName() {
        return jpClassName;
    }

    public Object getThirdPartyData(String id) {
        return thirdPartyData.get(id);
    }

    public void addThirdPartyData(String id, Object object) {
        this.thirdPartyData.put(id, object);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeGenSpec)) return false;
        CodeGenSpec that = (CodeGenSpec) o;
        return Objects.equals(packageName, that.packageName) && Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, className);
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
        ENUM("Enum", Enum.class, Enum.class),

        /* Containers */
        ARRAY(null, null, null),
        COLLECTION(null, null, null),
        MAP(null, null, null),
        OBJECT("ObjectAsBytes", null, null),

        BYTE_ARRAY("ObjectAsBytes", byte[].class, null),

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
        LIST(List.class, ArrayList.class), SET(Set.class, HashSet.class), QUEUE(Queue.class, PriorityQueue.class), DEQUE(Deque.class, ArrayDeque.class);

        private final Class<?> intefaceClass;
        private final Class<?> implementationClass;

        CollectionType(Class<?> intefaceClass, Class<?> implementationClass) {
            this.intefaceClass = intefaceClass;
            this.implementationClass = implementationClass;
        }

        public Class<?> getInterfaceClass() {
            return intefaceClass;
        }

        public Class<?> getImplementationClass() {
            return implementationClass;
        }
    }

    public static class FieldSpec {
        String name;
        int index;
        Type type;
        String getter;
        String setter;
        // set to false if getters and setters are not methods
        boolean hasBeanMethods = true;

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

        public boolean hasBeanMethods() {
            return hasBeanMethods;
        }

        public void setHasBeanMethods(boolean hasBeanMethods) {
            this.hasBeanMethods = hasBeanMethods;
        }
    }

    public static class Type {
        private DataType nativeType;
        private List<Type> containerTypes;
        private CodeGenSpec referenceType;
        private CollectionType collectionType;
        private String genericTypeSignature;

        // javapoet pre-computes
        private TypeName jPTypeName;

        private ClassName newInstanceType;

        public DataType getNativeType() {
            return nativeType;
        }

        public void setNativeType(DataType nativeType) {
            this.nativeType = nativeType;
        }

        public List<Type> getContainerTypes() {
            return containerTypes;
        }

        public void setContainerTypes(List<Type> containerTypes) {
            this.containerTypes = containerTypes;
        }

        public CodeGenSpec getReferenceType() {
            return referenceType;
        }

        public void setReferenceType(CodeGenSpec referenceType) {
            this.referenceType = referenceType;
        }

        public CollectionType getCollectionType() {
            return collectionType;
        }

        public void setCollectionType(CollectionType collectionType) {
            this.collectionType = collectionType;
        }

        public String getGenericTypeSignature() {
            return genericTypeSignature;
        }

        public void setGenericTypeSignature(String genericTypeSignature) {
            this.genericTypeSignature = genericTypeSignature;
        }

        public TypeName getjPTypeName() {
            return jPTypeName;
        }

        public void setjPTypeName(TypeName jPTypeName) {
            this.jPTypeName = jPTypeName;
        }

        public ClassName getNewInstanceType() {
            return newInstanceType;
        }

        public void setNewInstanceType(ClassName newInstanceType) {
            this.newInstanceType = newInstanceType;
        }
    }
}
