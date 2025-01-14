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

import com.flipkart.pibify.codegen.log.SpecGenLog;
import com.flipkart.pibify.codegen.log.SpecGenLogLevel;

import java.util.Collection;

/**
 * This interface is designed for creating a CodeGenSpec from an input POJO class.
 * Author bageshwar.pn
 * Date 09/08/24
 */
public interface ICodeGenSpecCreator {

    /*

    There can be different strategies to create a CodeGenSpec based in type of input

    1. Create via vanilla reflection in class loaded in classloader
    2. Create via class received from annotation processor

    Only 1 for testing right now
     */
    CodeGenSpec create(Class<?> type) throws CodeGenException;

    /**
     * To clear any internal state of the implementation. Can be called when reusing a creator instance
     */
    void resetState();


    Collection<SpecGenLog> getLogsForCurrentEntity();

    Collection<SpecGenLog> getLogsForCurrentEntity(Class<?> entity);

    SpecGenLogLevel status(Class<?> entity);
}
