package ru.otus.architect.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessCheckCommandExpressionTest {

    @Mock
    private Expression<Command> success;
    @Mock
    private Expression<Command> failure;
    @Mock
    private ExpressionContext context;

    private Expression<Command> accessCheckCommandExpression;

    @BeforeEach
    void setUp() {
        accessCheckCommandExpression = new AccessCheckCommandExpression(success, failure);
    }

    @Test
    @DisplayName("Если доступ разрешен вернеть команду success")
    void interpretSuccess() {
        when(context.resolve("User.GameObject::hasAccess", context.getMessage())).thenReturn(true);

        accessCheckCommandExpression.interpret(context);

        verify(success, times(1)).interpret(context);
    }

    @Test
    @DisplayName("Если доступ запрещен вернеть команду failure")
    void interpretFailure() {
        when(context.resolve("User.GameObject::hasAccess", context.getMessage())).thenReturn(false);

        accessCheckCommandExpression.interpret(context);

        verify(failure, times(1)).interpret(context);
    }
}