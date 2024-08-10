package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.codegen.PibifyCodeExecException;

/**
 * This class is the base stub for serializers
 * Author bageshwar.pn
 * Date 10/08/24
 */
public abstract class PibifyGenerated<T> {
    public abstract byte[] serialize(T object) throws PibifyCodeExecException;

    public abstract T deserialize(byte[] bytes) throws PibifyCodeExecException;
}
