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
class ActionNameExpressionTest {

    private final static String ACTION_NAME = "test";

    @Mock
    private ExpressionContext context;
    @Mock
    private Message message;

    private Expression<String> expression;

    @BeforeEach
    void setUp() {
        expression = new ActionNameExpression();
    }

    @Test
    void interpret() {
        when(context.getMessage()).thenReturn(message);
        when(context.resolve("IoC.Game::getActionName", message)).thenReturn(ACTION_NAME);

        var result = expression.interpret(context);

        assertEquals(ACTION_NAME, result);
        verify(context, times(1)).resolve("IoC.Game::getActionName", message);
    }
}