package ru.otus.architect.processor.visitors;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor9;

public class IoCPluginValueVisitor extends SimpleAnnotationValueVisitor9<Class<?>, Void>{

    @Override
    public Class<?> visitType(TypeMirror t, Void unused) {
        try {
            return Class.forName(t.toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
