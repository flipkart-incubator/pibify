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

package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;
import com.flipkart.pibify.core.PibifyClassMetadata;

/**
 * This class is used for testing reserved indicices
 * Author bageshwar.pn
 * Date 25/07/24
 */

@PibifyClassMetadata(reservedIndices = {2, 3})
public class ClassWithSimpleFields {

    @Pibify(1)
    public String aString = "1";

    @Pibify(2)
    public int anInt = 2;

    @Pibify(3)
    public long aLong = 3L;

}
