package com.flipkart.pibify.toskip;

import com.flipkart.pibify.core.Pibify;

/**
 * This enum is used for
 * Author bageshwar.pn
 * Date 05/10/24
 */
public enum EnumWithSomeMembers2 {

    @Pibify(1)
    Value1, @Pibify(2)
    Value2, @Pibify(value = 3)
    Value3, @Pibify(value = 4)
    Value4
}
