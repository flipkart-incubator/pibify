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
