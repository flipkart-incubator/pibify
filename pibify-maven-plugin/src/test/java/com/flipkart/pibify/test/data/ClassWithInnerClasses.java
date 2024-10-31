package com.flipkart.pibify.test.data;

import com.flipkart.pibify.core.Pibify;

import java.util.Objects;

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

    @Pibify(4)
    private StaticInnerClass staticInnerClass2;

    public ClassWithInnerClasses randomize() {
        str1 = "str" + Math.random();
        staticInnerClass = new StaticInnerClass().randomize();
        staticInnerClass2 = new StaticInnerClass().randomize();
        return this;
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

    public StaticInnerClass getStaticInnerClass2() {
        return staticInnerClass2;
    }

    public void setStaticInnerClass2(StaticInnerClass staticInnerClass2) {
        this.staticInnerClass2 = staticInnerClass2;
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

        public StaticInnerClass randomize() {
            str3 = "str" + Math.random();
            staticInnerInnerClass = new StaticInnerInnerClass();
            staticInnerInnerClass.str33 = "str" + Math.random();
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StaticInnerClass)) return false;
            StaticInnerClass that = (StaticInnerClass) o;
            return Objects.equals(str3, that.str3) && Objects.equals(staticInnerInnerClass, that.staticInnerInnerClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(str3, staticInnerInnerClass);
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

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof StaticInnerInnerClass)) return false;
                StaticInnerInnerClass that = (StaticInnerInnerClass) o;
                return Objects.equals(str33, that.str33);
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(str33);
            }
        }
    }
}
