package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for testing serde of abstract classes as references of a model
 * Author bageshwar.pn
 * Date 08/12/24
 */
public class ClassWithAbstractClassAsReference {

    @Pibify(1)
    public AbstractClassWithNativeFields abstractReference1;

    @Pibify(2)
    public AbstractClassWithNativeFields abstractReference2;

    public void randomize() {
        abstractReference1 = new ConcreteClassWithNativeFields();
        abstractReference1.randomize();

        abstractReference2 = new ConcreteClassBWithNativeFields();
        abstractReference2.randomize();
    }

}
