package com.flipkart.pibify.test.data.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.test.data.AbstractClassWithNativeFields;
import com.flipkart.pibify.test.data.ConcreteClassBWithNativeFields;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Pibify(3)
    public double aDouble;

    @Pibify(4)
    public List<String> list;

    @Pibify(5)
    public Map<BigDecimal, Double> map;

    @Pibify(6)
    public AbstractClassWithNativeFields ref1;

    @Pibify(7)
    private double[] doubles;

    @Pibify(8)
    private String[] strings;

    @Pibify(9)
    private ConcreteClassBWithNativeFields[] refs;

    // TODO Support for arrays of abstract classes

    @JsonCreator
    public ClassWithJsonCreator(@JsonProperty("bigDecimal") BigDecimal bigDecimal,
                                @JsonProperty("aDouble") double aDouble,
                                @JsonProperty("aString") String aString,
                                @JsonProperty("map") Map<BigDecimal, Double> map,
                                @JsonProperty("ref1") AbstractClassWithNativeFields ref1,
                                @JsonProperty("doubles") double[] doubles,
                                @JsonProperty("strings") String[] strings,
                                @JsonProperty("refs") ConcreteClassBWithNativeFields[] refs,
                                @JsonProperty("list") List<String> list) {
        this.aString = aString;
        this.bigDecimal = bigDecimal;
        this.aDouble = aDouble;
        this.list = list;
        this.map = map;
        this.ref1 = ref1;
        this.doubles = doubles;
        this.strings = strings;
        this.refs = refs;
    }

    public static ClassWithJsonCreator randomize() {
        Map<BigDecimal, Double> map = new HashMap<>();
        map.put(BigDecimal.valueOf(Math.random()), Math.random());
        map.put(BigDecimal.valueOf(Math.random()), Math.random());
        map.put(BigDecimal.valueOf(Math.random()), Math.random());
        return new ClassWithJsonCreator(BigDecimal.valueOf(Math.random()),
                Math.random(),
                "str" + Math.random(),
                map,
                new ConcreteClassBWithNativeFields().randomize(),
                new double[]{Math.random(), Math.random(), Math.random()},
                new String[]{"str" + Math.random(), "str" + Math.random(), "str" + Math.random()},
                new ConcreteClassBWithNativeFields[]{new ConcreteClassBWithNativeFields().randomize(), new ConcreteClassBWithNativeFields().randomize()},
                Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random()));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassWithJsonCreator)) return false;
        ClassWithJsonCreator that = (ClassWithJsonCreator) o;
        return Double.compare(aDouble, that.aDouble) == 0 && Objects.equals(aString, that.aString) && Objects.equals(bigDecimal, that.bigDecimal) && Objects.equals(list, that.list) && Objects.equals(map, that.map) && Objects.equals(ref1, that.ref1) && Objects.deepEquals(doubles, that.doubles) && Objects.deepEquals(strings, that.strings) && Objects.deepEquals(refs, that.refs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aString, bigDecimal, aDouble, list, map, ref1, Arrays.hashCode(doubles), Arrays.hashCode(strings), Arrays.hashCode(refs));
    }

    public String getaString() {
        return aString;
    }


    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public double getaDouble() {
        return aDouble;
    }

    public List<String> getList() {
        return list;
    }

    public Map<BigDecimal, Double> getMap() {
        return map;
    }

    public AbstractClassWithNativeFields getRef1() {
        return ref1;
    }

    public double[] getDoubles() {
        return doubles;
    }

    public String[] getStrings() {
        return strings;
    }

    public ConcreteClassBWithNativeFields[] getRefs() {
        return refs;
    }
}
