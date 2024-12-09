package com.flipkart.pibify.test.data.lombok;

import com.flipkart.pibify.core.Pibify;
import lombok.Data;

import java.util.Objects;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 09/12/24
 */
@Data
public class BooleanOnLombok {

    @Pibify(1)
    private boolean supported;

    @Pibify(2)
    private boolean isEnabled;

    @Pibify(3)
    private Boolean supportedObject;

    @Pibify(4)
    private Boolean isEnabledObject;

    public static BooleanOnLombok randomize() {
        BooleanOnLombok object = new BooleanOnLombok();
        object.supported = Math.random() > 0.5;
        object.isEnabled = Math.random() > 0.5;
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BooleanOnLombok)) return false;
        BooleanOnLombok that = (BooleanOnLombok) o;
        return supported == that.supported && isEnabled == that.isEnabled && Objects.equals(supportedObject, that.supportedObject) && Objects.equals(isEnabledObject, that.isEnabledObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supported, isEnabled, supportedObject, isEnabledObject);
    }
}
