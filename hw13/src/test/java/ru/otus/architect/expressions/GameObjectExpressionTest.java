package ru.otus.architect.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.openapi.model.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameObjectExpressionTest {

    private final static Object OBJECT = new Object();

    @Mock
    private ExpressionContext context;
    @Mock
    private Message message;

    private Expression<Object> expression;

    @BeforeEach
    void setUp() {
        expression = new GameObjectExpression();
    }

    @Test
    void interpret() {
        when(context.getMessage()).thenReturn(message);
        when(context.resolve("IoC.Game::getGameObject", message)).thenReturn(OBJECT);

        var result = expression.interpret(context);

        assertEquals(OBJECT, result);
        verify(context, times(1)).resolve("IoC.Game::getGameObject", message);
    }
}