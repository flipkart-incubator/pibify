package com.flipkart.pibify.test.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.pibify.core.Pibify;

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
public class ClassWithJsonCreatorWithSetters {

    @Pibify(3)
    public double aDouble;
    @Pibify(4)
    public List<String> list;
    @Pibify(5)
    public Map<BigDecimal, Double> map;
    @Pibify(6)
    public AbstractClassWithNativeFields ref1;
    @Pibify(1)
    private String aString;
    @Pibify(2)
    private BigDecimal bigDecimal;

    @JsonCreator
    public ClassWithJsonCreatorWithSetters(@JsonProperty("bigDecimal") BigDecimal bigDecimal,
                                           @JsonProperty("aDouble") double aDouble,
                                           @JsonProperty("aString") String aString,
                                           @JsonProperty("map") Map<BigDecimal, Double> map,
                                           @JsonProperty("ref1") AbstractClassWithNativeFields ref1,
                                           @JsonProperty("list") List<String> list) {
        this.aString = aString;
        this.bigDecimal = bigDecimal;
        this.aDouble = aDouble;
        this.list = list;
        this.map = map;
        this.ref1 = ref1;
    }

    public ClassWithJsonCreatorWithSetters() {
    }

    public static ClassWithJsonCreatorWithSetters randomize() {
        Map<BigDecimal, Double> map = new HashMap<>();
        map.put(BigDecimal.valueOf(Math.random()), Math.random());
        map.put(BigDecimal.valueOf(Math.random()), Math.random());
        map.put(BigDecimal.valueOf(Math.random()), Math.random());
        return new ClassWithJsonCreatorWithSetters(BigDecimal.valueOf(Math.random()),
                Math.random(),
                "str" + Math.random(),
                map,
                new ConcreteClassBWithNativeFields().randomize(),
                Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random()));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassWithJsonCreatorWithSetters)) return false;
        ClassWithJsonCreatorWithSetters that = (ClassWithJsonCreatorWithSetters) o;
        return Double.compare(aDouble, that.aDouble) == 0 && Objects.equals(aString, that.aString) && Objects.equals(bigDecimal, that.bigDecimal) && Objects.equals(list, that.list) && Objects.equals(map, that.map) && Objects.equals(ref1, that.ref1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aString, bigDecimal, aDouble, list, map, ref1);
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

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<BigDecimal, Double> getMap() {
        return map;
    }

    public void setMap(Map<BigDecimal, Double> map) {
        this.map = map;
    }

    public AbstractClassWithNativeFields getRef1() {
        return ref1;
    }

    public void setRef1(AbstractClassWithNativeFields ref1) {
        this.ref1 = ref1;
    }
}
