package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author bageshwar.pn
 * Date 15/08/24
 */
public class ClassWithNativeCollectionsOfCollections {

    @Pibify(1)
    private List<List<String>> aString;

    @Pibify(2)
    private Set<Set<Integer>> anInt;

    @Pibify(3)
    private Map<Float, Map<String, Boolean>> aMap;

    public ClassWithNativeCollectionsOfCollections randomize() {
        aString = new ArrayList<>();
        aString.add(Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random()));
        aString.add(Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random()));
        aString.add(Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random()));
        anInt = new HashSet<>();
        anInt.add(new HashSet<>(Arrays.asList((int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000))));
        anInt.add(new HashSet<>(Arrays.asList((int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000))));
        anInt.add(new HashSet<>(Arrays.asList((int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000))));

        aMap = new HashMap<>();
        aMap.put((float) Math.random(), new HashMap<String, Boolean>() {{
            put("str" + Math.random(), Math.random() > 0.5);
            put("str" + Math.random(), Math.random() > 0.5);
            put("str" + Math.random(), Math.random() > 0.5);
        }});

        return this;
    }

    public List<List<String>> getaString() {
        return aString;
    }

    public void setaString(List<List<String>> aString) {
        this.aString = aString;
    }

    public Set<Set<Integer>> getAnInt() {
        return anInt;
    }

    public void setAnInt(Set<Set<Integer>> anInt) {
        this.anInt = anInt;
    }

    public Map<Float, Map<String, Boolean>> getaMap() {
        return aMap;
    }

    public void setaMap(Map<Float, Map<String, Boolean>> aMap) {
        this.aMap = aMap;
    }
}
