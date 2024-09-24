package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.PibifyCodeExecException;
import com.flipkart.pibify.core.PibifyConfiguration;

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
    public abstract byte[] serialize(T object) throws PibifyCodeExecException;

    public abstract T deserialize(byte[] bytes) throws PibifyCodeExecException;

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
}
