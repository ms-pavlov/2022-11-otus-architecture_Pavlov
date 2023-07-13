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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdapterInvocationHandlerTest {

    private final static String TEST_METHOD_NAME = "getPosition";
    private final static Object TEST_OBJECT = new Object();
    private final static Object[] TEST_ARGS = List.of(TEST_OBJECT).toArray();
    private final static Object[] TEST_PARAM = prepareParams();

    @Mock
    private Object proxyObject;
    @Mock
    private BiFunction<String, Object[], Object> dependencyIoCStrategy;
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

        handler = new AdapterInvocationHandler(TEST_OBJECT, dependencyIoCStrategy, generatorFactory);
    }


    @Test
    @DisplayName("Обращается к IoC контейнеру")
    void invoke() {
        when(dependencyIoCStrategy.apply(TEST_METHOD_NAME, TEST_PARAM)).thenReturn(TEST_OBJECT);

        var result = handler.invoke(proxyObject, method, TEST_ARGS);

        assertEquals(TEST_OBJECT, result);
        verify(generator, times(1)).getDependencyName(TEST_METHOD_NAME);
        verify(dependencyIoCStrategy, times(1)).apply(TEST_METHOD_NAME, TEST_PARAM);
    }

    private static Object[] prepareParams() {
        var params = new LinkedList<>(List.of(TEST_ARGS));
        params.addFirst(TEST_OBJECT);
        return params.toArray();
    }
}