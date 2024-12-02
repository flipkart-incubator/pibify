package com.flipkart.pibify.codegen.stub;

import com.flipkart.pibify.core.Pibify;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to hold a context for serialization/deserialization
 * Author bageshwar.pn
 * Date 29/11/24
 */
public class SerializationContext {

    @Pibify(2)
    private final List<String> dictionary;
    // Private internal state
    private final BiMap<String, Integer> mapOfWords;
    @Pibify(1)
    private short version;

    public SerializationContext() {
        dictionary = new ArrayList<>();
        mapOfWords = HashBiMap.create();
        version = 1;
    }

    public List<String> getDictionary() {
        dictionary.clear();
        // insert into specific index in the list and return it.
        // rebuilding this list everytime (assuming this method is called only once during serialization)
        mapOfWords.forEach((k, v) -> dictionary.add(v, k));
        return dictionary;
    }

    public int addStringToDictionary(String str) {
        return mapOfWords.computeIfAbsent(str, k -> mapOfWords.size());
    }

    public String getWord(int index) {
        return mapOfWords.inverse().get(index);
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }
}
