package com.flipkart.pibify.test.data.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 08/12/24
 */
public class BothConstructorAndNoSetter {

    @Pibify(1)
    private String aString;

    @Pibify(2)
    private Double aDouble;

    public BothConstructorAndNoSetter() {
        // no-op
    }

    @JsonCreator
    public BothConstructorAndNoSetter(@JsonProperty("aString") String aString,
                                      @JsonProperty("aDouble") Double aDouble) {
        this.aString = aString;
        this.aDouble = aDouble;
    }

    public static BothConstructorAndNoSetter randomize() {
        return new BothConstructorAndNoSetter("str" + Math.random(), Math.random());
    }

    public String getaString() {
        return aString;
    }

    public Double getaDouble() {
        return aDouble;
    }
}
