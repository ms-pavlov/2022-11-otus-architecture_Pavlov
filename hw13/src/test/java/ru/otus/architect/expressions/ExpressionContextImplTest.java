package ru.otus.architect.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.api.AnswerConsumer;
import ru.otus.openapi.model.Message;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpressionContextImplTest {

    private final static String TEST_DEPENDENCY = "test";
    private final static Object OBJECT = new Object();

    @Mock
    private BiFunction<String, Object[], Object> dependencyIoCStrategy;
    @Mock
    private Message message;
    @Mock
    private AnswerConsumer answerConsumer;

    private ExpressionContext context;


    @BeforeEach
    void setUp() {
        context = new ExpressionContextImpl(dependencyIoCStrategy, message, answerConsumer);
    }

    @Test
    void resolve() {
        when(dependencyIoCStrategy.apply(eq(TEST_DEPENDENCY), any())).thenReturn(OBJECT);

        var result = context.resolve(TEST_DEPENDENCY);

        assertEquals(OBJECT, result);
        verify(dependencyIoCStrategy, times(1)).apply(eq(TEST_DEPENDENCY), any());
    }

    @Test
    void getMessage() {
        assertEquals(message, context.getMessage());
    }

    @Test
    void getAnswerConsumer() {
        assertEquals(answerConsumer, context.getAnswerConsumer());
    }
}