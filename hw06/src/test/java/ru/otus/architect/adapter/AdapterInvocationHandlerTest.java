package ru.otus.architect.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.processor.DependencyNameGenerator;
import ru.otus.architect.processor.DependencyNameGeneratorFactory;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdapterInvocationHandlerTest {

    private final static String TEST_METHOD_NAME = "getPosition";
    private final static Object[] TEST_ARGS = new Object[] {};
    private final static Object TEST_OBJECT = new Object();

    @Mock
    private Object object;
    @Mock
    private Object proxyObject;
    @Mock
    private IoCContainer container;
    @Mock
    private DependencyNameGeneratorFactory generatorFactory;
    private Method method;
    @Mock
    private DependencyNameGenerator generator;

    private AdapterInvocationHandler handler;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        when(generatorFactory.create(any(), any())).thenReturn(generator);
        when(generator.getDependencyName(any())).thenReturn(TEST_METHOD_NAME);

        method = Movable.class.getMethod(TEST_METHOD_NAME);

        handler = new AdapterInvocationHandler(object, container, generatorFactory);
    }


    @Test
    @DisplayName("Обращается к IoC контейнеру")
    void invoke() {
        when(container.resolve(TEST_METHOD_NAME, object)).thenReturn(TEST_OBJECT);

        var result = handler.invoke(proxyObject, method, TEST_ARGS);

        assertEquals(TEST_OBJECT, result);
        verify(generator, times(1)).getDependencyName(TEST_METHOD_NAME);
        verify(container, times(1)).resolve(TEST_METHOD_NAME, object);
    }
}