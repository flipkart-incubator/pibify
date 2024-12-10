package com.flipkart.pibify.test.data.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;
import lombok.Getter;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 08/12/24
 */
@Getter
public class RenamedBooleanInConstructor {

    @Pibify(1)
    private String aString;

    @Pibify(2)
    private boolean isAllowed;

    @Pibify(3)
    private boolean hasFlag;

    @JsonCreator
    public RenamedBooleanInConstructor(@JsonProperty("aString") String aString,
                                       @JsonProperty("allowed") boolean isAllowed,
                                       @JsonProperty("hasFlag") boolean flag) {
        this.aString = aString;
        this.isAllowed = isAllowed;
        this.hasFlag = flag;
    }

    public static RenamedBooleanInConstructor randomize() {
        return new RenamedBooleanInConstructor(
                "str" + Math.random(),
                Math.random() > 0.5,
                Math.random() > 0.5
        );
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RenamedBooleanInConstructor)) return false;
        RenamedBooleanInConstructor that = (RenamedBooleanInConstructor) o;
        return isAllowed == that.isAllowed && hasFlag == that.hasFlag && Objects.equals(aString, that.aString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aString, isAllowed, hasFlag);
    }
}
