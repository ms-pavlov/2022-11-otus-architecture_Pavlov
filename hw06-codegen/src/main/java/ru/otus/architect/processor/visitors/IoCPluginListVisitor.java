package ru.otus.architect.processor.visitors;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.util.SimpleAnnotationValueVisitor9;
import java.util.List;

public class IoCPluginListVisitor extends SimpleAnnotationValueVisitor9<List<? extends Class<?>>, Void> {

    @Override
    public List<? extends Class<?>> visitArray(List<? extends AnnotationValue> values, Void unused) {
        System.out.println();
        return values.stream()
                .map(annotationValue -> annotationValue.accept(new IoCPluginValueVisitor(), null))
                .toList();
    }
}
