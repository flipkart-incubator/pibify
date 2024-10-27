package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;
import java.util.TreeMap;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 12/10/24
 */
public class MapClassLevel3<K> extends TreeMap<K, String> {

    @Pibify(1)
    private String str;

    public void randomize(K key) {
        str = "str" + Math.random();
        this.put(key, "str" + Math.random());
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapClassLevel3)) return false;
        if (!super.equals(o)) return false;

        MapClassLevel3<?> that = (MapClassLevel3<?>) o;
        return Objects.equals(str, that.str);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(str);
        return result;
    }
}
