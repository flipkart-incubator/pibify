package com.flipkart.pibify.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * This class is used for processing the pibify annotation
 * Author bageshwar.pn
 * Date 25/07/24
 */

@SupportedAnnotationTypes("com.flipkart.pibify.core.Pibify")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class PibifyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : annotatedElements) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "warn", element);
                //element.asType().accept(new TypeVisitor(), null);
                element.accept(new ElementVisitor(), roundEnv);
            }
        }

        return false;
    }

    private static class ElementVisitor implements javax.lang.model.element.ElementVisitor<Object, RoundEnvironment> {
        @Override
        public Object visit(Element e, RoundEnvironment o) {
            return null;
        }

        @Override
        public Object visit(Element e) {
            return null;
        }

        @Override
        public Object visitPackage(PackageElement e, RoundEnvironment o) {
            return null;
        }

        @Override
        public Object visitType(TypeElement e, RoundEnvironment o) {
            return null;
        }

        @Override
        public Object visitVariable(VariableElement e, RoundEnvironment o) {
            // e.getSimpleName().toString() gives the name of the member variable
            // e.asType().toString() gives the fqdn
            // use PibifyProcessor.this.processingEnvironment to access utils to handle types
            return null;
        }

        @Override
        public Object visitExecutable(ExecutableElement e, RoundEnvironment o) {
            return null;
        }

        @Override
        public Object visitTypeParameter(TypeParameterElement e, RoundEnvironment o) {
            return null;
        }

        @Override
        public Object visitUnknown(Element e, RoundEnvironment o) {
            return null;
        }
    }
}
