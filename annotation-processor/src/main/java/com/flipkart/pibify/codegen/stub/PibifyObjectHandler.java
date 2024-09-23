package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.CodeGenUtil;
import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.serde.PibifyDeserializer;
import com.flipkart.pibify.serde.PibifySerializer;

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

    @Override
    public byte[] serialize(Object object) throws PibifyCodeExecException {
        ISerializer serializer = new PibifySerializer();
        try {
            // Write the name of the class as the first param
            String refType = object.getClass().getName();
            serializer.writeString(1, refType);

            PibifyGenerated refHandler = (PibifyGenerated) Class.forName(
                    CodeGenUtil.PIBIFY_GENERATED_PACKAGE_NAME + refType + "Handler").newInstance();

            // Write the object binary as the second attribute.
            serializer.writeObjectAsBytes(2, refHandler.serialize(object));
            return serializer.serialize();
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws PibifyCodeExecException {
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
                    case 16:
                        if (refType == null) {
                            throw new IllegalStateException("Reference type unknown before handling bytes");
                        }
                        PibifyGenerated refHandler = (PibifyGenerated) Class.forName(
                                CodeGenUtil.PIBIFY_GENERATED_PACKAGE_NAME + refType + "Handler").newInstance();
                        object = refHandler.deserialize(deserializer.readObjectAsBytes());
                        break;
                    default:
                        throw new UnsupportedOperationException("Unable to find tag in gen code");
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
