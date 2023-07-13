package ru.otus.architect.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.objects.characteristic.Movable;

import java.lang.reflect.InvocationHandler;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdapterGeneratorImplTest {

    private final static Object TEST_OBJECT = new Object();

    @Mock
    private Object object;
    @Mock
    private InvocationHandlerProvider handlerProvider;
    @Mock
    private InvocationHandler handler;
    private AdapterGenerator generator;

    @BeforeEach
    void setUp() {
        when(handlerProvider.getHandler(any())).thenReturn(handler);
        generator = new AdapterGeneratorImpl(handlerProvider);
    }



    @Test
    void generate() {
        var result = generator.generate(Movable.class, object);


        assertTrue(List.of(result.getClass().getInterfaces()).contains(Movable.class));
    }
}