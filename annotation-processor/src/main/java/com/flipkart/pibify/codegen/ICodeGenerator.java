package com.flipkart.pibify.codegen;

import com.squareup.javapoet.JavaFile;

import java.io.IOException;

/**
 * This interface is designed for generating code based in the input CodeGenSpec
 * Author bageshwar.pn
 * Date 09/08/24
 */
public interface ICodeGenerator {
    JavaFile generate(CodeGenSpec codeGenSpec) throws IOException, CodeGenException;
}
