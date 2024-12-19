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
