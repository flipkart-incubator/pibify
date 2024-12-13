package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.core.PibifyConfiguration;
import com.flipkart.pibify.serde.IDeserializer;
import com.flipkart.pibify.serde.ISerializer;
import com.flipkart.pibify.serde.PibifyDeserializer;
import com.flipkart.pibify.serde.PibifySerializer;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.protobuf.WireFormat;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class is the base stub for serializers
 * Author bageshwar.pn
 * Date 10/08/24
 */
public abstract class PibifyGenerated<T> {

    protected static final int DEFAULT_OBJECT_SIZE = 256;

    // TODO add detailed comments about each serialize/deserialize method variation
    public abstract void serialize(T object, ISerializer serializer, SerializationContext context) throws PibifyCodeExecException;

    public byte[] serialize(T object) throws PibifyCodeExecException {
        ISerializer serializer = new PibifySerializer(getEstimatedObjectSize());
        try {
            serializer.writeObjectAsBytes(1, Ints.toByteArray(Integer.MAX_VALUE));
            SerializationContext context = new SerializationContext();
            this.serialize(object, serializer, context);
            byte[] bytes = serializer.serialize();

            // Get the bytes for context, and add them to the end
            PibifySerializer contextSerializer = new PibifySerializer(getEstimatedObjectSize());
            SerializationContextHandlerHolder.serializationContextHandler
                    .serialize(context, contextSerializer, null);

            byte[] contextBytes = contextSerializer.serialize();
            byte[] concatenated = Bytes.concat(bytes, contextBytes);
            // Tag at idx=0, and size of byte array(4) at idx=1, hence starting from 2
            int i = 2;
            for (byte aByte : Ints.toByteArray(bytes.length)) {
                concatenated[i++] = aByte;
            }

            return concatenated;
        } catch (Exception e) {
            throw new PibifyCodeExecException(e);
        }
    }

    protected static int getEndObjectTag() {
        return (1 << 3) | WireFormat.WIRETYPE_END_GROUP;
    }

    public abstract T deserialize(IDeserializer deserializer, Class<T> type, SerializationContext context) throws PibifyCodeExecException;

    public T deserialize(byte[] bytes) throws PibifyCodeExecException {
        return deserialize(bytes, null);
    }

    public int getEstimatedObjectSize() {
        return DEFAULT_OBJECT_SIZE;
    }

    public T deserialize(IDeserializer deserializer) throws PibifyCodeExecException {
        return this.deserialize(deserializer, null, new SerializationContext());
    }

    public T deserialize(IDeserializer deserializer, SerializationContext context) throws PibifyCodeExecException {
        return this.deserialize(deserializer, null, context);
    }

    public T deserialize(byte[] bytes, Class<T> type) throws PibifyCodeExecException {
        IDeserializer pibifyDeserializer = new PibifyDeserializer(bytes);
        try {

            int tag = pibifyDeserializer.getNextTag();
            int idx = Ints.fromByteArray(pibifyDeserializer.readObjectAsBytes());
            byte[] subArray = Arrays.copyOfRange(bytes, idx, bytes.length);
            SerializationContext serializationContext = SerializationContextHandlerHolder.serializationContextHandler
                    .deserialize(new PibifyDeserializer(subArray), null, null);

            // Create a serializer that excludes
            //  1) the context meta at the start (tag+idx) and
            //  2) the context object towards the end
            pibifyDeserializer = new PibifyDeserializer(Arrays.copyOfRange(bytes, 6, idx));

            return deserialize(pibifyDeserializer, type, serializationContext);
        } catch (IOException e) {
            throw new PibifyCodeExecException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <A> void handleArrayDeserialization(Class<A> type, Supplier<A[]> objectGetter,
                                                  Function<A[], Void> objectSetter,
                                                  Supplier<A> deserializerGetter) {
        // TODO Improve performance of this impl
        // A better impl would create a list and read all values upfront
        // and dump into the array. That would need some state management in the parent switch-case block
        A[] newArray;
        A[] oldArray = objectGetter.get();
        A val = deserializerGetter.get();
        if (oldArray == null) {
            newArray = (A[]) Array.newInstance(type, 1);
            newArray[0] = val;
        } else {
            newArray = Arrays.copyOf(oldArray, oldArray.length + 1);
            newArray[oldArray.length] = val;
        }
        objectSetter.apply(newArray);
    }

    protected <A> void handleCollectionDeserialization(Class<A> type,
                                                       Supplier<Collection<A>> objectGetter,
                                                       Function<Collection<A>, Void> objectSetter,
                                                       Supplier<Collection<A>> collectionCreator,
                                                       Supplier<A> deserializerGetter) {
        if (objectGetter.get() == null) {
            objectSetter.apply(collectionCreator.get());
        }

        objectGetter.get().add(deserializerGetter.get());
    }

    protected <E> E getEnumValue(E[] enums, int index) throws PibifyCodeExecException {
        if (index < 0 || index >= enums.length) {
            if (PibifyConfiguration.instance().ignoreUnknownEnums()) {
                return null;
            } else {
                throw new PibifyCodeExecException("Unknown Enum Cardinal " + index + " for " + enums[0].getClass().getCanonicalName());
            }
        } else {
            return enums[index];
        }
    }

    private static final class SerializationContextHandlerHolder {
        // Lazy load to avoid class loading deadlock
        static final PibifyGenerated<SerializationContext> serializationContextHandler = new SerializationContextHandler();
    }

    /**
     * This method to be implemented by generated code to initialize fields (references to handlers)
     */
    public void initialize() {

    }
}
