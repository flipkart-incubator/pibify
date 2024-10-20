package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

import java.math.BigDecimal;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 19/10/24
 */
public class GenericMapFields {

    @Pibify(1)
    public MapClassLevel3<Double> doubleToString;

    @Pibify(2)
    public MapClassLevel3<BigDecimal> bigDecimalToString;


    public void randomize() {
        doubleToString = new MapClassLevel3<>();
        doubleToString.randomize(Math.random());

        bigDecimalToString = new MapClassLevel3<>();
        bigDecimalToString.randomize(BigDecimal.valueOf(Math.random()));
    }
}
