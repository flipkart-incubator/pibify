package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 08/09/24
 */
public class ClassWithCollectionsOfEnums {

    @Pibify(1)
    private List<EnumB> listOfEnums;

    @Pibify(2)
    private Map<ClassWithEnums.EnumA, EnumB> mapOfEnums;

    @Pibify(3)
    private Map<EnumB, Map<String, ClassWithEnums.EnumA>> enumMap;

    @Pibify(4)
    private EnumB[] arrayOfEnums;

    public void randomize() {
        listOfEnums = Arrays.asList(
                EnumB.values()[((int) (Math.random() * 10)) % 3],
                EnumB.values()[((int) (Math.random() * 10)) % 3],
                EnumB.values()[((int) (Math.random() * 10)) % 3]);

        mapOfEnums = new HashMap<>();
        mapOfEnums.put(ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3],
                EnumB.values()[((int) (Math.random() * 10)) % 3]);
        mapOfEnums.put(ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3],
                EnumB.values()[((int) (Math.random() * 10)) % 3]);
        mapOfEnums.put(ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3],
                EnumB.values()[((int) (Math.random() * 10)) % 3]);

        enumMap = new EnumMap<>(EnumB.class);
        Map<String, ClassWithEnums.EnumA> tmp = new HashMap<>();
        tmp.put("str" + Math.random(), ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3]);
        tmp.put("str" + Math.random(), ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3]);
        tmp.put("str" + Math.random(), ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3]);
        enumMap.put(EnumB.values()[((int) (Math.random() * 10)) % 3], tmp);

        tmp = new HashMap<>();
        tmp.put("str" + Math.random(), ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3]);
        tmp.put("str" + Math.random(), ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3]);
        tmp.put("str" + Math.random(), ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3]);
        enumMap.put(EnumB.values()[((int) (Math.random() * 10)) % 3], tmp);

        tmp = new HashMap<>();
        tmp.put("str" + Math.random(), ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3]);
        tmp.put("str" + Math.random(), ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3]);
        tmp.put("str" + Math.random(), ClassWithEnums.EnumA.values()[((int) (Math.random() * 10)) % 3]);
        enumMap.put(EnumB.values()[((int) (Math.random() * 10)) % 3], tmp);

        arrayOfEnums = new EnumB[]{
                EnumB.values()[((int) (Math.random() * 10)) % 3],
                EnumB.values()[((int) (Math.random() * 10)) % 3],
                EnumB.values()[((int) (Math.random() * 10)) % 3]
        };

    }

    public List<EnumB> getListOfEnums() {
        return listOfEnums;
    }

    public void setListOfEnums(List<EnumB> listOfEnums) {
        this.listOfEnums = listOfEnums;
    }

    public Map<ClassWithEnums.EnumA, EnumB> getMapOfEnums() {
        return mapOfEnums;
    }

    public void setMapOfEnums(Map<ClassWithEnums.EnumA, EnumB> mapOfEnums) {
        this.mapOfEnums = mapOfEnums;
    }

    public Map<EnumB, Map<String, ClassWithEnums.EnumA>> getEnumMap() {
        return enumMap;
    }

    public void setEnumMap(Map<EnumB, Map<String, ClassWithEnums.EnumA>> enumMap) {
        this.enumMap = enumMap;
    }

    public EnumB[] getArrayOfEnums() {
        return arrayOfEnums;
    }

    public void setArrayOfEnums(EnumB[] arrayOfEnums) {
        this.arrayOfEnums = arrayOfEnums;
    }
}
