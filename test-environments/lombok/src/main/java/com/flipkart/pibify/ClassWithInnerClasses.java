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

package com.flipkart.pibify;

import com.flipkart.pibify.core.Pibify;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 10/09/24
 */
public class ClassWithInnerClasses {

    @Pibify(1)
    private String str1;

    @Pibify(3)
    private StaticInnerClass staticInnerClass;

    public void randomize() {
        str1 = "str" + Math.random();

        staticInnerClass = new StaticInnerClass();
        staticInnerClass.randomize();
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public StaticInnerClass getStaticInnerClass() {
        return staticInnerClass;
    }

    public void setStaticInnerClass(StaticInnerClass staticInnerClass) {
        this.staticInnerClass = staticInnerClass;
    }

    public static class StaticInnerClass {
        @Pibify(1)
        private String str3;

        @Pibify(2)
        private StaticInnerInnerClass staticInnerInnerClass;

        public String getStr3() {
            return str3;
        }

        public void setStr3(String str3) {
            this.str3 = str3;
        }

        public StaticInnerInnerClass getStaticInnerInnerClass() {
            return staticInnerInnerClass;
        }

        public void setStaticInnerInnerClass(StaticInnerInnerClass staticInnerInnerClass) {
            this.staticInnerInnerClass = staticInnerInnerClass;
        }

        public void randomize() {
            str3 = "str" + Math.random();
            staticInnerInnerClass = new StaticInnerInnerClass();
            staticInnerInnerClass.str33 = "str" + Math.random();
        }

        public static class StaticInnerInnerClass {
            @Pibify(1)
            private String str33;

            public String getStr33() {
                return str33;
            }

            public void setStr33(String str33) {
                this.str33 = str33;
            }
        }
    }
}
