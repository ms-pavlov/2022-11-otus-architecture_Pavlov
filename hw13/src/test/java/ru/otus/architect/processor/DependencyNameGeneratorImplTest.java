package ru.otus.architect.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DependencyNameGeneratorImplTest {
    private final static Class<?> TEST_CLASS = Object.class;
    private final static String TEST_INTERFACE_NAME = "Test";
    private final static String TEST_METHOD_NAME = "get";
    private final static String TEST_PLUGIN_NAME = TEST_CLASS.getSimpleName() + TEST_INTERFACE_NAME + "AdapterPlugin";
    private final static String TEST_DEPENDENCY_NAME = "IoC." + TEST_CLASS.getSimpleName() + "." + TEST_INTERFACE_NAME + "::" + TEST_METHOD_NAME;

    private DependencyNameGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new DependencyNameGeneratorImpl(TEST_CLASS.getSimpleName(), TEST_INTERFACE_NAME);
    }

    @Test
    @DisplayName("Генерирует название класса плагина")
    void getPluginClassName() {
        assertEquals(TEST_PLUGIN_NAME, generator.getPluginClassName());
    }

    @Test
    @DisplayName("Генерирует название зависимости для метода")
    void getDependencyName() {
        assertEquals(TEST_DEPENDENCY_NAME, generator.getDependencyName(TEST_METHOD_NAME));
    }
}