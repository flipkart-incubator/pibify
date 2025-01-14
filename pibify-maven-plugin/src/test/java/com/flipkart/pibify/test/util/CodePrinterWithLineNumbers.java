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

package com.flipkart.pibify.test.util;

import java.io.IOException;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 29/10/24
 */
public class CodePrinterWithLineNumbers implements Appendable {
    private static final Appendable underlying = System.out;
    private final boolean printLineNumbers;
    private int line = 1;

    public CodePrinterWithLineNumbers() {
        this(false);
    }

    public CodePrinterWithLineNumbers(boolean printLineNumbers) {
        this.printLineNumbers = printLineNumbers;
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
        if (printLineNumbers && csq.toString().contains("\n")) {
            underlying.append(csq);
            return underlying.append((++line) + "");
        } else {
            return underlying.append(csq);
        }

    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return underlying.append(csq, start, end);
    }

    @Override
    public Appendable append(char c) throws IOException {
        if (printLineNumbers && c == 10) {
            underlying.append(c);
            return underlying.append((line++) + "");
        } else {
            return underlying.append(c);
        }

    }
}
