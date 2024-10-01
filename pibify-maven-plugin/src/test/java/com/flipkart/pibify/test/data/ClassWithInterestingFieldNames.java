package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;
import lombok.Data;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 30/09/24
 */
@Data
public class ClassWithInterestingFieldNames {

    @Pibify(1)
    private String a;

    @Pibify(2)
    private String anApple;

    @Pibify(3)
    private String aMango;

    @Pibify(4)
    private String a1;

    @Pibify(5)
    private String a1Apple;

    public void randomize() {
        a = "str" + Math.random();
        anApple = "str2" + Math.random();
        aMango = "str2" + Math.random();
        a1 = "str2" + Math.random();
        a1Apple = "str2" + Math.random();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassWithInterestingFieldNames that = (ClassWithInterestingFieldNames) o;
        return Objects.equals(a, that.a) && Objects.equals(anApple, that.anApple) && Objects.equals(aMango, that.aMango) && Objects.equals(a1, that.a1) && Objects.equals(a1Apple, that.a1Apple);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(a);
        result = 31 * result + Objects.hashCode(anApple);
        result = 31 * result + Objects.hashCode(aMango);
        result = 31 * result + Objects.hashCode(a1);
        result = 31 * result + Objects.hashCode(a1Apple);
        return result;
    }
}
