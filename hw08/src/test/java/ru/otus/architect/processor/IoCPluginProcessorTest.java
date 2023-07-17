package ru.otus.architect.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IoCPluginProcessorTest {
    private final static List<Class<?>> TEST_CLASSES = List.of(Object.class);
    private final static String TEST_INTERFACE_NAME = "Object";
    private final static String TEST_PLUGIN_NAME = "ObjectObjectAdapterPlugin";

    @Mock
    private TypeElement typeElement;
    @Mock
    private RoundEnvironment roundEnv;
    @Mock
    private Element element;
    @Mock
    private ExecutableElement key;
    @Mock
    private AnnotationValue value;
    @Mock
    private ProcessingEnvironment environment;
    @Mock
    private AnnotationMirror annotationMirror;
    @Mock
    private Name name;
    @Mock
    private Filer filer;
    @Mock
    private JavaFileObject fileObject;
    @Mock
    private Writer writer;

    @Spy
    private IoCAdapterPluginProcessor processor;

    @BeforeEach
    void setUp() {
        when(roundEnv.getElementsAnnotatedWith(typeElement))
                .thenReturn((Set) Set.of(element));
        when(element.getAnnotationMirrors())
                .thenReturn((List) List.of(annotationMirror));
        when(annotationMirror.getElementValues())
                .thenReturn((Map) Map.of(key, value));
        when(element.getSimpleName()).thenReturn(name);
        when(name.toString()).thenReturn(TEST_INTERFACE_NAME);

        when(key.toString()).thenReturn("classes()");
        when(value.accept(any(), any())).thenReturn(TEST_CLASSES);

        processor = new IoCAdapterPluginProcessor();
    }

    @Test
    @DisplayName("Если processingEnv и есть элементы для обработки, то создается файл")
    void process() throws IOException {
        when(environment.getFiler()).thenReturn(filer);
        when(filer.createSourceFile(eq(TEST_PLUGIN_NAME))).thenReturn(fileObject);
        when(fileObject.openWriter()).thenReturn(writer);
        processor.init(environment);

        assertTrue(processor.process(Set.of(typeElement), roundEnv));

        verify(roundEnv, times(1)).getElementsAnnotatedWith(typeElement);
        verify(environment, times(1)).getFiler();
        verify(filer, times(1)).createSourceFile(eq(TEST_PLUGIN_NAME));
        verify(fileObject, times(1)).openWriter();
    }

    @Test
    @DisplayName("Если processingEnv не задано и есть элементы для обработки, то вернет исключение")
    void processIOException() {

        assertThrows(RuntimeException.class, () -> processor.process(Set.of(typeElement), roundEnv));
    }
}