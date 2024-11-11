package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.core.Constants;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.serde.PibifyDeserializer;
import com.flipkart.pibify.serde.PibifySerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap;

/**
 * This class is used for handling Object references. Since we don't have enough reflected information in some cases,
 * the object handler is used, which writes the type of the reference to the stream along with the object.
 * During deserialization, type info is unpacked and handled accordingly.
 * Author bageshwar.pn
 * Date 15/08/24
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PibifyObjectHandler extends PibifyGenerated<Object> {

    // hacky flag to control the flow of `getRefClassForTest()`
    public static boolean forTest = false;
    // Cache to look-up for the handler at runtime
    private final AbstractPibifyHandlerCache pibifyHandlerCache;

    public PibifyObjectHandler(AbstractPibifyHandlerCache pibifyHandlerCache) {
        this.pibifyHandlerCache = pibifyHandlerCache;
    }

    private PibifyGenerated<?> getRefClassForTest(String refType, Class<?> objectClass) throws Exception {

        if (forTest) {
            String className = Constants.PIBIFY_GENERATED_PACKAGE_NAME + refType + "Handler";
            // Test code added because the adhoc compiled classes are not available to the test-runner class loader
            Class<?> compilerClass = Class.forName("com.flipkart.pibify.test.util.SimpleCompiler");
            Field instance = compilerClass.getField("INSTANCE");
            Object compilerInstance = instance.get(null);
            Method loadClassMethod = compilerClass.getMethod("loadClass", String.class);
            Class<PibifyGenerated<?>> generatedClass = (Class<PibifyGenerated<?>>) loadClassMethod.invoke(compilerInstance, className);
            return generatedClass.getDeclaredConstructor().newInstance();
        } else {
            return pibifyHandlerCache.getHandler(objectClass).get();
        }
    }

    @Override
    public byte[] serialize(Object object) throws PibifyCodeExecException {
        if (object == null) {
            return null;
        }

        ISerializer serializer = new PibifySerializer();
        try {
            // Write the name of the class as the first param
            Class<?> objectClass = object.getClass();
            String refType = objectClass.getName();
            serializer.writeString(1, refType);

            if (objectClass.equals(String.class)) {
                serializer.writeString(2, (String) object);
            } else if (objectClass.equals(Short.class)) {
                serializer.writeShort(2, (Short) object);
            } else if (objectClass.equals(Boolean.class)) {
                serializer.writeBool(2, (Boolean) object);
            } else if (objectClass.equals(Byte.class)) {
                serializer.writeByte(2, (Byte) object);
            } else if (objectClass.equals(Character.class)) {
                serializer.writeChar(2, (Character) object);
            } else if (objectClass.equals(Integer.class)) {
                serializer.writeInt(2, (Integer) object);
            } else if (objectClass.equals(Long.class)) {
                serializer.writeLong(2, (Long) object);
            } else if (objectClass.equals(Float.class)) {
                serializer.writeFloat(2, (Float) object);
            } else if (objectClass.equals(Double.class)) {
                serializer.writeDouble(2, (Double) object);
            } else if (Enum.class.isAssignableFrom(objectClass)) {
                serializer.writeEnum(2, (Enum) object);
            } else {
                PibifyGenerated refHandler = getRefClassForTest(refType, objectClass);
                // Write the object binary as the second attribute.
                serializer.writeObjectAsBytes(2, refHandler.serialize(object));
            }

            return serializer.serialize();
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class clazzType) throws PibifyCodeExecException {
        try {
            Object object = null;
            IDeserializer deserializer = new PibifyDeserializer(bytes);
            int tag = deserializer.getNextTag();
            String refType = null;
            while (tag != 0) {
                switch (tag) {
                    case 10:
                        refType = deserializer.readString();
                        break;
                    // WARN: This operation is unsafe. We are attempting to read from the stream without
                    // caring for the tag.
                    default:
                        if (refType == null) {
                            throw new IllegalStateException("Reference type unknown before handling bytes");
                        }

                        Class<?> objectClass = Class.forName(refType);

                        if (objectClass.equals(String.class)) {
                            object = deserializer.readString();
                        } else if (objectClass.equals(Short.class)) {
                            object = (short) deserializer.readShort();
                        } else if (objectClass.equals(Boolean.class)) {
                            object = deserializer.readBool();
                        } else if (objectClass.equals(Byte.class)) {
                            object = (byte) deserializer.readByte();
                        } else if (objectClass.equals(Character.class)) {
                            object = (char) deserializer.readChar();
                        } else if (objectClass.equals(Integer.class)) {
                            object = deserializer.readInt();
                        } else if (objectClass.equals(Long.class)) {
                            object = deserializer.readLong();
                        } else if (objectClass.equals(Float.class)) {
                            object = deserializer.readFloat();
                        } else if (objectClass.equals(Double.class)) {
                            object = deserializer.readDouble();
                        } else if (Enum.class.isAssignableFrom(objectClass)) {
                            object = deserializer.readEnum();
                        } else {
                            PibifyGenerated refHandler = getRefClassForTest(refType, objectClass);
                            object = refHandler.deserialize(deserializer.readObjectAsBytes(), objectClass);
                        }

                        break;
                }
                tag = deserializer.getNextTag();
            }
            // Hacking the return type being `object` to return a tuple.
            // This helps keep this class stateless and the object and its class type gets returned in one go
            // Class type is used for typecasting at caller
            return new AbstractMap.SimpleEntry<>(refType, object);
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }
}
