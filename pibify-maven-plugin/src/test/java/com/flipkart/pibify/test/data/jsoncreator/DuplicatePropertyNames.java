package com.flipkart.pibify.test.data.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 08/12/24
 */
public class DuplicatePropertyNames {

    @Pibify(1)
    private String aString;

    @Pibify(2)
    private String aString1;

    @Pibify(3)
    private Double aDouble;

    @JsonCreator
    public DuplicatePropertyNames(@JsonProperty("aString") String aString,
                                  @JsonProperty("aString") String aString1,
                                  @JsonProperty("aDouble") Double aDouble) {
        this.aString = aString;
        this.aString1 = aString1;
        this.aDouble = aDouble;
    }

    public String getaString() {
        return aString;
    }

    public String getaString1() {
        return aString1;
    }

    public Double getaDouble() {
        return aDouble;
    }
}
