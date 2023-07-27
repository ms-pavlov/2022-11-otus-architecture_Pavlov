package ru.otus.architect.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.objects.characteristic.Movable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdapterExpressionTest {

    private final static Object TEST_OBJECT = new Object();
    private final static Class<?> TEST_CLAZZ = Movable.class;
    @Mock
    private ExpressionContext context;
    @Mock
    private Expression<Object> objectExpression;
    @Mock
    private Movable movable;

    private Expression<?> expression;

    @BeforeEach
    void setUp() {
        expression = new AdapterExpression<>(TEST_CLAZZ, objectExpression);
    }

    @Test
    void interpret() {
        when(objectExpression.interpret(context)).thenReturn(TEST_OBJECT);
        when(context.resolve("Adapter", TEST_CLAZZ, TEST_OBJECT)).thenReturn(movable);

        var result = expression.interpret(context);

        assertEquals(movable, result);
        verify(objectExpression, times(1)).interpret(context);
        verify(context, times(1)).resolve("Adapter", TEST_CLAZZ, TEST_OBJECT);

    }
}