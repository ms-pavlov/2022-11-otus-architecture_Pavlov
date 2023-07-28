package ru.otus.architect.processor.visitors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IoCPluginListVisitorTest {
    private final static Class<?> TEST_CLASS = Object.class;

    @Mock
    private AnnotationValue annotationValue;

    private AnnotationValueVisitor<List<? extends Class<?>>, Void> visitor;

    @BeforeEach
    void setUp() {
        visitor = new IoCPluginListVisitor();
    }

    @Test
    @DisplayName("Можно получить список параметров у аннотации")
    void visitType() {
        when(annotationValue.accept(any(), any())).thenReturn(TEST_CLASS);

        var result = visitor.visitArray(List.of(annotationValue), null);

        assertEquals(1, result.size());
        assertTrue(result.contains(TEST_CLASS));
    }
}