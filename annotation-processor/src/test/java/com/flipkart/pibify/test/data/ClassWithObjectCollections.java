package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.test.data.another.AnotherClassWithNativeFields;

import java.util.Arrays;
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
}
