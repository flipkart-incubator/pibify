package com.flipkart.pibify.codegen;

/**
 * This interface is designed for creating a CodeGenSpec from an input POJO class.
 * Author bageshwar.pn
 * Date 09/08/24
 */
public interface ICodeGenSpecCreator {

    /*

    There can be different strategies to create a CodeGenSpec based in type of input

    1. Create via vanilla reflection in class loaded in classloader
    2. Create via class received from annotation processor

    Only 1 for testing right now
     */
    CodeGenSpec create(Class<?> type) throws CodeGenException;
}
