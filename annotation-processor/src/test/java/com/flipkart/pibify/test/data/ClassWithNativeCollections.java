package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

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
public class ClassWithNativeCollections {

    @Pibify(1)
    private List<String> aString;

    @Pibify(2)
    private Set<Integer> anInt;

    @Pibify(3)
    private Map<Float, Boolean> aMap;

    /*
    @Pibify(4)
    private Collection<?> aCollection;
     */

    public ClassWithNativeCollections randomize() {
        aString = Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random());
        anInt = new HashSet<>(Arrays.asList(
                (int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000)));

        aMap = new HashMap<>();
        aMap.put((float) Math.random(), Math.random() > 0.5);
        aMap.put((float) Math.random(), Math.random() > 0.5);
        aMap.put((float) Math.random(), Math.random() > 0.5);
        return this;
    }


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
}
