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

package com.flipkart.pibify.codegen;

import java.io.IOException;

/**
 * This interface is designed for generating code based in the input CodeGenSpec
 * Author bageshwar.pn
 * Date 09/08/24
 */
public interface ICodeGenerator {
    JavaFileWrapper generate(CodeGenSpec codeGenSpec) throws IOException, CodeGenException;
}
