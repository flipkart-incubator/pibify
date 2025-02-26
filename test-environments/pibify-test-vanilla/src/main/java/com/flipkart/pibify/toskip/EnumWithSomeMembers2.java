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
