package com.flipkart.pibify.test.data.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 08/12/24
 */
public class PartialConstructorWithSetters {

    @Pibify(1)
    private String aString;

    @Pibify(2)
    private String aString2;

    @Pibify(3)
    private Double aDouble;

    @JsonCreator
    public PartialConstructorWithSetters(@JsonProperty("aString") String aString,
                                         @JsonProperty("aDouble") Double aDouble) {
        this.aString = aString;
        this.aDouble = aDouble;
    }

    public String getaString() {
        return aString;
    }

    public String getaString2() {
        return aString2;
    }

    public void setaString2(String aString2) {
        this.aString2 = aString2;
    }

    public Double getaDouble() {
        return aDouble;
    }
}
