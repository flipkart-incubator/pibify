package com.flipkart.pibify.test.data.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 08/12/24
 */
public class MismatchedPropertyNames {

    @Pibify(1)
    private String aString;

    @Pibify(2)
    private Double aDouble;

    @JsonCreator
    public MismatchedPropertyNames(@JsonProperty("aString1") String aString,
                                   @JsonProperty("aDouble") Double aDouble) {
        this.aString = aString;
        this.aDouble = aDouble;
    }

    public String getaString() {
        return aString;
    }

    public Double getaDouble() {
        return aDouble;
    }
}
