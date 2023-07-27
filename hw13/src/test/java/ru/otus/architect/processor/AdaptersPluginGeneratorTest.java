package ru.otus.architect.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdaptersPluginGeneratorTest {
    private final static Class<?> TEST_CLASS = Object.class;
    private final static String TEST_CLASS_NAME = "ObjectObjectAdapterPlugin";
    private final static String TEST_DEPENDENCY_NAME = "IoC.Object.Object::get";
    private final static String TEST_METHOD_NAME = "get";
    @Mock
    private Element interfaceClass;
    @Mock
    private PrintWriter writer;
    @Mock
    private Element interfaceMethod;
    @Mock
    private Name interfaceMethodName;
    @Mock
    private DependencyNameGenerator nameGenerator;


    private PluginGenerator generator;

    @BeforeEach
    void setUp() {
        when(nameGenerator.getPluginClassName()).thenReturn(TEST_CLASS_NAME);
        when(nameGenerator.getDependencyName(TEST_METHOD_NAME)).thenReturn(TEST_DEPENDENCY_NAME);

        generator = new AdaptersPluginGenerator(TEST_CLASS, interfaceClass, writer, nameGenerator);
    }

    @Test
    void prepPlugin() {
        when(interfaceClass.getEnclosedElements()).thenReturn((List) List.of(interfaceMethod));
        when(interfaceMethod.getSimpleName()).thenReturn(interfaceMethodName);
        when(interfaceMethodName.toString()).thenReturn(TEST_METHOD_NAME);

        var result = generator.prepPlugin();

        assertNotNull(result);

        verify(interfaceClass, times(1)).getEnclosedElements();
        verify(nameGenerator, times(1)).getPluginClassName();
        verify(nameGenerator, times(1)).getDependencyName(TEST_METHOD_NAME);
    }
}