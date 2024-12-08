package com.flipkart.pibify.test.data.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 08/12/24
 */
public class PartialConstructorWithNonPibify {

    @Pibify(1)
    public String aString;

    public String aString2;

    @Pibify(3)
    public Double aDouble;

    @JsonCreator
    public PartialConstructorWithNonPibify(@JsonProperty("aString") String aString,
                                           @JsonProperty("aDouble") Double aDouble) {
        this.aString = aString;
        this.aDouble = aDouble;
    }
}
