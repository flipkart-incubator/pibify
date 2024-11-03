package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class AnotherGenericClass<T> {

    @Pibify(1)
    public Map<Double, List<AGenericClass<T>>> list;

    public void randomize(T value1, T value2, T value3) {
        this.list = new HashMap<>();
        this.list.put(Math.random(), Collections.singletonList(AGenericClass.randomize(value1)));
        this.list.put(Math.random(), Collections.singletonList(AGenericClass.randomize(value2)));
        this.list.put(Math.random(), Collections.singletonList(AGenericClass.randomize(value3)));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AnotherGenericClass)) return false;
        AnotherGenericClass<?> that = (AnotherGenericClass<?>) object;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(list);
    }
}
