package com.flipkart.pibify.test.data.generics;

import com.flipkart.pibify.core.Pibify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 02/11/24
 */
public class ClassWithReferenceToGenericClass {

    @Pibify(1)
    public AGenericClass<String> reference;

    @Pibify(2)
    public List<AGenericClass<Double>> referenceList;

    @Pibify(3)
    public Map<AGenericClass<BigDecimal>, AGenericClass<String>> aMap;

    @Pibify(4)
    public GenericClassWithMultipleParameters<String, BigDecimal, AGenericClass<AGenericClass<String>>> multiParamObject;

    @Pibify(5)
    public List<GenericClassWithMultipleParameters<String, BigDecimal, AGenericClass<Double>>> multiList;

    @Pibify(6)
    public Map<
            GenericClassWithMultipleParameters<String, BigDecimal, AGenericClass<String>>,
            GenericClassWithMultipleParameters<BigDecimal, String, AGenericClass<Double>>
            > multiMap;

    public void randomize() {
        reference = AGenericClass.randomize("str" + Math.random());

        referenceList = new ArrayList<>();
        referenceList.add(AGenericClass.randomize(Math.random()));
        referenceList.add(AGenericClass.randomize(Math.random()));
        referenceList.add(AGenericClass.randomize(Math.random()));

        aMap = new HashMap<>();
        aMap.put(AGenericClass.randomize(BigDecimal.valueOf(Math.random())), AGenericClass.randomize("str" + Math.random()));
        aMap.put(AGenericClass.randomize(BigDecimal.valueOf(Math.random())), AGenericClass.randomize("str" + Math.random()));
        aMap.put(AGenericClass.randomize(BigDecimal.valueOf(Math.random())), AGenericClass.randomize("str" + Math.random()));

        multiParamObject = GenericClassWithMultipleParameters.randomize(
                "str" + Math.random(),
                BigDecimal.valueOf(Math.random()),
                AGenericClass.randomize(AGenericClass.randomize("str" + Math.random()))
        );

        multiList = new ArrayList<>();
        multiList.add(GenericClassWithMultipleParameters.randomize("str" + Math.random(), BigDecimal.valueOf(Math.random()), AGenericClass.randomize(Math.random())));
        multiList.add(GenericClassWithMultipleParameters.randomize("str" + Math.random(), BigDecimal.valueOf(Math.random()), AGenericClass.randomize(Math.random())));
        multiList.add(GenericClassWithMultipleParameters.randomize("str" + Math.random(), BigDecimal.valueOf(Math.random()), AGenericClass.randomize(Math.random())));

        multiMap = new MapClassLevel1<>();
        multiMap.put(GenericClassWithMultipleParameters.randomize("str" + Math.random(), BigDecimal.valueOf(Math.random()), AGenericClass.randomize("str" + Math.random())),
                GenericClassWithMultipleParameters.randomize(BigDecimal.valueOf(Math.random()), "str" + Math.random(), AGenericClass.randomize(Math.random())));
        multiMap.put(GenericClassWithMultipleParameters.randomize("str" + Math.random(), BigDecimal.valueOf(Math.random()), AGenericClass.randomize("str" + Math.random())),
                GenericClassWithMultipleParameters.randomize(BigDecimal.valueOf(Math.random()), "str" + Math.random(), AGenericClass.randomize(Math.random())));
        multiMap.put(GenericClassWithMultipleParameters.randomize("str" + Math.random(), BigDecimal.valueOf(Math.random()), AGenericClass.randomize("str" + Math.random())),
                GenericClassWithMultipleParameters.randomize(BigDecimal.valueOf(Math.random()), "str" + Math.random(), AGenericClass.randomize(Math.random())));

    }
}
