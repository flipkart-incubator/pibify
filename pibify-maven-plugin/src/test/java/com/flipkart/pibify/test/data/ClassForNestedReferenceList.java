package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 30/10/24
 */
public class ClassForNestedReferenceList {

    @Pibify(1)
    public List<Sentence> words;

    @Pibify(2)
    public List<ArrayList<String>> listOfArrayLists;

    public void randomize() {
        words = new ArrayList<>();
        words.add(new Sentence().randomize());
        words.add(new Sentence().randomize());
        words.add(new Sentence().randomize());

        listOfArrayLists = new ArrayList<>();
        listOfArrayLists.add(new Sentence().randomize());
        listOfArrayLists.add(new Sentence().randomize());
        // setting a impl for testing
        listOfArrayLists.add(new ArrayList<>());
    }
}

