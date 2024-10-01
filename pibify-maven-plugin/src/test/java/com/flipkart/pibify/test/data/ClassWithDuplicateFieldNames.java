package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;
import lombok.Data;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 30/09/24
 */
@Data
public class ClassWithDuplicateFieldNames {

    @Pibify(1)
    private String anApple;

    @Pibify(2)
    private String AnApple;
}
