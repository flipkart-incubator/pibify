package com.flipkart.pibify.codegen;

import com.flipkart.pibify.core.Pibify;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is used for testing native collections handling
 * Author bageshwar.pn
 * Date 09/08/24
 */
public class ClassWithNativeCollections {

    @Pibify(1)
    private List<String> aString;

    @Pibify(2)
    private Set<Integer> anInt;

    //@Pibify(3)
    private Map<Float, Boolean> aMap;

    @Pibify(4)
    private Collection<?> aCollection;


    public List<String> getaString() {
        return aString;
    }

    public void setaString(List<String> aString) {
        this.aString = aString;
    }

    public Set<Integer> getAnInt() {
        return anInt;
    }

    public void setAnInt(Set<Integer> anInt) {
        this.anInt = anInt;
    }

    public Map<Float, Boolean> getaMap() {
        return aMap;
    }

    public void setaMap(Map<Float, Boolean> aMap) {
        this.aMap = aMap;
    }

    public Collection<?> getaCollection() {
        return aCollection;
    }

    public void setaCollection(Collection<?> aCollection) {
        this.aCollection = aCollection;
    }
}
