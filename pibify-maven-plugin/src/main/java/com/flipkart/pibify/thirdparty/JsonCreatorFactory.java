package com.flipkart.pibify.thirdparty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.flipkart.pibify.ThirdPartyProcessorResult;
import com.flipkart.pibify.codegen.CodeGenSpec;
import com.flipkart.pibify.codegen.ThirdPartyProcessor;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used for handling the classes which have a @JsonCreator annotation
 * Author bageshwar.pn
 * Date 31/10/24
 */
public class JsonCreatorFactory implements ThirdPartyProcessor {

    @Override
    public ThirdPartyProcessorResult process(CodeGenSpec codeGenSpec, Class<?> type) {
        ThirdPartyProcessorResult result = new ThirdPartyProcessorResult();
        List<Constructor<?>> constructors = Arrays.stream(type.getDeclaredConstructors())
                .filter(c -> c.getAnnotation(JsonCreator.class) != null)
                .collect(Collectors.toList());

        // TODO implement JsonCreatorFactory

        return result;
    }

    @Override
    public String getId() {
        return this.getClass().getName();
    }

    public static class JsonCreatorFactoryData {

    }
}
