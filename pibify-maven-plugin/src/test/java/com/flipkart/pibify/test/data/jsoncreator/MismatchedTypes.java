package com.flipkart.pibify.test.data.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 08/12/24
 */
public class MismatchedTypes {

    @Pibify(1)
    public String aString;

    @Pibify(2)
    public List<Double> aDouble;

    @JsonCreator
    public MismatchedTypes(@JsonProperty("aString") String aString,
                           @JsonProperty("aDouble") List aDouble) {
        this.aString = aString;
        this.aDouble = aDouble;
    }

    public static MismatchedTypes randomize() {
        return new MismatchedTypes("str" + Math.random(),
                Arrays.asList(Math.random(), Math.random(), Math.random()));
    }
}
