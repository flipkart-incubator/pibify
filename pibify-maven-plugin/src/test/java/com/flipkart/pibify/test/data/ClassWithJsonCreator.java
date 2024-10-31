package com.flipkart.pibify.test.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;

import java.math.BigDecimal;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 31/10/24
 */
public class ClassWithJsonCreator {

    @Pibify(1)
    private String aString;

    @Pibify(2)
    private BigDecimal bigDecimal;

    @JsonCreator
    public ClassWithJsonCreator(@JsonProperty("bigDecimal") BigDecimal bigDecimal,
                                @JsonProperty("aString") String aString) {
        this.aString = aString;
        this.bigDecimal = bigDecimal;
    }

    public static ClassWithJsonCreator randomize() {
        return new ClassWithJsonCreator(BigDecimal.valueOf(Math.random()), "str" + Math.random());
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }
}
