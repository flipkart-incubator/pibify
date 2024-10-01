package com.flipkart.pibify;

import com.flipkart.pibify.core.PibifyConfiguration;

import java.lang.reflect.Field;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 24/09/24
 */
public class TestUtils {
    public static void resetConfig() {
        try {
            Field instance = PibifyConfiguration.class.getDeclaredField("INSTANCE");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
