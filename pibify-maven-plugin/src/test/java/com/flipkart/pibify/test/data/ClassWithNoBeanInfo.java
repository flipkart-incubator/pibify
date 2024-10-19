package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 17/10/24
 */
public class ClassWithNoBeanInfo {

    @Pibify(1)
    public String str1;
    @Pibify(3)
    public List<String> list;
    @Pibify(4)
    public EnumB enumB;
    @Pibify(5)
    public int[] intArray;
    @Pibify(6)
    public Map<String, Boolean> aMap;
    @Pibify(2)
    private String str2;

    @Pibify(7)
    public BigDecimal bigDecimal;

    public void randomize() {
        str1 = "str" + Math.random();
        str2 = "str" + Math.random();
        list = Arrays.asList("str" + Math.random());
        enumB = EnumB.values()[((int) (Math.random() * 10)) % 3];
        intArray = new int[]{(int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000)};

        aMap = new HashMap<String, Boolean>() {{
            put("str" + Math.random(), Math.random() > 0.5);
            put("str" + Math.random(), Math.random() > 0.5);
            put("str" + Math.random(), Math.random() > 0.5);
        }};

        bigDecimal = new BigDecimal(Math.random());
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }
}
