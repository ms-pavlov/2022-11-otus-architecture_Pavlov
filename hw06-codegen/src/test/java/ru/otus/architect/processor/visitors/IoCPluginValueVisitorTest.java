package ru.otus.architect.processor.visitors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.type.TypeMirror;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IoCPluginValueVisitorTest {
    private final static Class<?> TEST_CLASS = Object.class;
    private final static String BAD_CLASS_NAME = "";
    @Mock
    private TypeMirror typeMirror;

    private AnnotationValueVisitor<Class<?>, Void> visitor;

    @BeforeEach
    void setUp() {
        visitor = new IoCPluginValueVisitor();
    }

    @Test
    @DisplayName("Можно получить класс из значения")
    void visitType() {
        when(typeMirror.toString()).thenReturn(TEST_CLASS.getName());

        var result = visitor.visitType(typeMirror, null);

        assertEquals(TEST_CLASS, result);
    }

    @Test
    @DisplayName("Можно получить класс из значения")
    void visitTypeException() {
        when(typeMirror.toString()).thenReturn(BAD_CLASS_NAME);

        assertThrows(RuntimeException.class, () -> visitor.visitType(typeMirror, null));
    }
}