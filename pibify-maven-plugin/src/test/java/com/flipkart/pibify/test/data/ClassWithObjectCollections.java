package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeFields;
import com.flipkart.pibify.test.data.generics.TertiaryGenericClassForList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is used for testing native collections handling
 * Author bageshwar.pn
 * Date 09/08/24
 */
public class ClassWithObjectCollections {

    @Pibify(1)
    private List<ClassWithNativeFields> nativeFields;

    @Pibify(2)
    private Set<ClassWithAutoboxFields> autoboxFields;

    @Pibify(3)
    private AnotherClassWithNativeFields[] arrayOfOtherNativeFields;

    @Pibify(4)
    private Map<ClassWithNativeFields, ClassWithAutoboxFields> mapOfObjects;

    @Pibify(5)
    private List<BigDecimal> bigDecimalList;

    @Pibify(6)
    private Map<BigDecimal, Date> bigDecimalMap;

    @Pibify(7)
    public List<AbstractClassWithNativeFields> listOfAbstractClass;

    @Pibify(8)
    public Map<AbstractClassWithNativeFields, AbstractClassWithNativeFields> mapOfAbstractValues;

    @Pibify(9)
    public TertiaryGenericClassForList<String> tertiaryGenericList;

    /*
    @Pibify(4)
    private Collection<?> aCollection;
     */

    public void randomize() {
        nativeFields = Arrays.asList(new ClassWithNativeFields().randomize(), new ClassWithNativeFields().randomize(), new ClassWithNativeFields().randomize());
        autoboxFields = new HashSet<>(Arrays.asList(new ClassWithAutoboxFields().randomize(), new ClassWithAutoboxFields().randomize(), new ClassWithAutoboxFields().randomize()));
        arrayOfOtherNativeFields = new AnotherClassWithNativeFields[]{new AnotherClassWithNativeFields().randomize(), new AnotherClassWithNativeFields().randomize(), new AnotherClassWithNativeFields().randomize()};
        mapOfObjects = new HashMap<>();
        mapOfObjects.put(new ClassWithNativeFields().randomize(), new ClassWithAutoboxFields().randomize());
        mapOfObjects.put(new ClassWithNativeFields().randomize(), new ClassWithAutoboxFields().randomize());
        mapOfObjects.put(new ClassWithNativeFields().randomize(), new ClassWithAutoboxFields().randomize());
        mapOfObjects.put(new ClassWithNativeFields().randomize(), new ClassWithAutoboxFields().randomize());
        bigDecimalList = Arrays.asList(new BigDecimal(Math.random()), new BigDecimal(Math.random()), new BigDecimal(Math.random()));

        bigDecimalMap = new HashMap<>();
        bigDecimalMap.put(new BigDecimal(Math.random()), new Date(System.currentTimeMillis() - (long) (Math.random() * 10000)));
        bigDecimalMap.put(new BigDecimal(Math.random()), new Date(System.currentTimeMillis() - (long) (Math.random() * 10000)));
        bigDecimalMap.put(new BigDecimal(Math.random()), new Date(System.currentTimeMillis() - (long) (Math.random() * 10000)));

        listOfAbstractClass = new ArrayList<>();
        listOfAbstractClass.add(new ConcreteClassWithNativeFields().randomize());
        listOfAbstractClass.add(new ConcreteClassWithNativeFields().randomize());
        listOfAbstractClass.add(new ConcreteClassWithNativeFields().randomize());

        mapOfAbstractValues = new HashMap<>();
        mapOfAbstractValues.put(new ConcreteClassWithNativeFields().randomize(), new ConcreteClassBWithNativeFields().randomize());
        mapOfAbstractValues.put(new ConcreteClassWithNativeFields().randomize(), new ConcreteClassBWithNativeFields().randomize());
        mapOfAbstractValues.put(new ConcreteClassWithNativeFields().randomize(), new ConcreteClassBWithNativeFields().randomize());

        tertiaryGenericList = new TertiaryGenericClassForList<>();
        tertiaryGenericList.randomize(Math.random() + "", Math.random() + "", Math.random() + "");
    }

    public List<ClassWithNativeFields> getNativeFields() {
        return nativeFields;
    }

    public void setNativeFields(List<ClassWithNativeFields> nativeFields) {
        this.nativeFields = nativeFields;
    }

    public Set<ClassWithAutoboxFields> getAutoboxFields() {
        return autoboxFields;
    }

    public void setAutoboxFields(Set<ClassWithAutoboxFields> autoboxFields) {
        this.autoboxFields = autoboxFields;
    }

    public AnotherClassWithNativeFields[] getArrayOfOtherNativeFields() {
        return arrayOfOtherNativeFields;
    }

    public void setArrayOfOtherNativeFields(AnotherClassWithNativeFields[] arrayOfOtherNativeFields) {
        this.arrayOfOtherNativeFields = arrayOfOtherNativeFields;
    }

    public Map<ClassWithNativeFields, ClassWithAutoboxFields> getMapOfObjects() {
        return mapOfObjects;
    }

    public void setMapOfObjects(Map<ClassWithNativeFields, ClassWithAutoboxFields> mapOfObjects) {
        this.mapOfObjects = mapOfObjects;
    }

    public List<BigDecimal> getBigDecimalList() {
        return bigDecimalList;
    }

    public void setBigDecimalList(List<BigDecimal> bigDecimalList) {
        this.bigDecimalList = bigDecimalList;
    }

    public Map<BigDecimal, Date> getBigDecimalMap() {
        return bigDecimalMap;
    }

    public void setBigDecimalMap(Map<BigDecimal, Date> bigDecimalMap) {
        this.bigDecimalMap = bigDecimalMap;
    }
}
