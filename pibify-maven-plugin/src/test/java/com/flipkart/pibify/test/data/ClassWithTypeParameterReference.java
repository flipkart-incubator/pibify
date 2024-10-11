package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 20/09/24
 */
public class ClassWithTypeParameterReference<T> {

    @Pibify(1)
    T genericTypeReference;

    @Pibify(2)
    List<T> list;

    @Pibify(3)
    List<List<T>> list2;

    @Pibify(4)
    Map<T, T> map;

    @Pibify(5)
    Map<List<T>, Set<T>> map2;

    public T getGenericTypeReference() {
        return genericTypeReference;
    }

    public void setGenericTypeReference(T genericTypeReference) {
        this.genericTypeReference = genericTypeReference;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<List<T>> getList2() {
        return list2;
    }

    public void setList2(List<List<T>> list2) {
        this.list2 = list2;
    }

    public Map<T, T> getMap() {
        return map;
    }

    public void setMap(Map<T, T> map) {
        this.map = map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassWithTypeParameterReference)) return false;

        ClassWithTypeParameterReference<?> that = (ClassWithTypeParameterReference<?>) o;
        return Objects.equals(genericTypeReference, that.genericTypeReference) && Objects.equals(list, that.list) && Objects.equals(list2, that.list2) && Objects.equals(map, that.map) && Objects.equals(map2, that.map2);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(genericTypeReference);
        result = 31 * result + Objects.hashCode(list);
        result = 31 * result + Objects.hashCode(list2);
        result = 31 * result + Objects.hashCode(map);
        result = 31 * result + Objects.hashCode(map2);
        return result;
    }

    public Map<List<T>, Set<T>> getMap2() {
        return map2;
    }

    public void setMap2(Map<List<T>, Set<T>> map2) {
        this.map2 = map2;
    }

}
