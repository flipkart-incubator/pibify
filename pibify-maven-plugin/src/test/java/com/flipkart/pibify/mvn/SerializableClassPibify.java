package com.flipkart.pibify.mvn;

import com.flipkart.pibify.core.Pibify;

import java.io.Serializable;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 11/10/24
 */
public class SerializableClassPibify implements Serializable {
    @Pibify(1)
    String str;
}
