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

package com.flipkart.pibify.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used for processing the pibify annotation
 * Author bageshwar.pn
 * Date 25/07/24
 */

@SupportedAnnotationTypes("com.flipkart.pibify.core.Pibify")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@AutoService(Processor.class)
public class PibifyProcessor extends AbstractProcessor {

    private final Set<String> classes;

    public PibifyProcessor() {
        classes = new HashSet<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : annotatedElements) {
                String ownerClass = element.getEnclosingElement().toString();
                // Preparing a list of classes that have to be processed
                if (!classes.contains(ownerClass)) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing " + ownerClass, null);
                    classes.add(ownerClass);
                }
            }
        }

        // don't let other annotation processors get triggered for this.
        return true;
    }
}
