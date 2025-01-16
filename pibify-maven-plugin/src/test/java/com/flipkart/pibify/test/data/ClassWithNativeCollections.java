/*
 *
 *  *Copyright [2025] [Original Author]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Pibify(4)
    private List<byte[]> listOfBytes;

    @Pibify(5)
    private HashMap<Float, Boolean> anotherMap;

    @Pibify(6)
    private ArrayList<String> anotherString;

    public ClassWithNativeCollections randomize() {
        aString = Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random());
        anotherString = new ArrayList<>(Arrays.asList("str" + Math.random(), "str" + Math.random(), "str" + Math.random()));
        anInt = new HashSet<>(Arrays.asList(
                (int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 1000)));

        aMap = new HashMap<>();
        aMap.put((float) Math.random(), Math.random() > 0.5);
        aMap.put((float) Math.random(), Math.random() > 0.5);
        aMap.put((float) Math.random(), Math.random() > 0.5);

        anotherMap = new HashMap<>();
        anotherMap.put((float) Math.random(), Math.random() > 0.5);
        anotherMap.put((float) Math.random(), Math.random() > 0.5);
        anotherMap.put((float) Math.random(), Math.random() > 0.5);

        listOfBytes = new ArrayList<>();
        listOfBytes.add(new byte[]{(byte) (Math.random() * 255), (byte) (Math.random() * 255), (byte) (Math.random() * 255)});
        listOfBytes.add(new byte[]{(byte) (Math.random() * 255), (byte) (Math.random() * 255), (byte) (Math.random() * 255)});
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

    public List<byte[]> getListOfBytes() {
        return listOfBytes;
    }

    public void setListOfBytes(List<byte[]> listOfBytes) {
        this.listOfBytes = listOfBytes;
    }

    public HashMap<Float, Boolean> getAnotherMap() {
        return anotherMap;
    }

    public void setAnotherMap(HashMap<Float, Boolean> anotherMap) {
        this.anotherMap = anotherMap;
    }

    public ArrayList<String> getAnotherString() {
        return anotherString;
    }

    public void setAnotherString(ArrayList<String> anotherString) {
        this.anotherString = anotherString;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassWithNativeCollections)) return false;
        ClassWithNativeCollections that = (ClassWithNativeCollections) o;
        return Objects.equals(aString, that.aString) && Objects.equals(anInt, that.anInt) && Objects.equals(aMap, that.aMap) && this.listEquals(that.listOfBytes) && Objects.equals(anotherMap, that.anotherMap) && Objects.equals(anotherString, that.anotherString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aString, anInt, aMap, listOfBytes, anotherMap, anotherString);
    }

    // compare to list of arrays
    public boolean listEquals(List<byte[]> listOfBytes) {

        if (this.listOfBytes == null && listOfBytes != null) return false;

        if (this.listOfBytes != null && listOfBytes == null) return false;

        if (this.listOfBytes == null) return true;

        if (this.listOfBytes.size() != listOfBytes.size()) return false;
        for (int i = 0; i < this.listOfBytes.size(); i++) {
            if (!Arrays.equals(this.listOfBytes.get(i), listOfBytes.get(i))) return false;
        }
        return true;
    }
}
