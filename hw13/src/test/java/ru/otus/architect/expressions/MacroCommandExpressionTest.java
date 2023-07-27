package ru.otus.architect.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MacroCommandExpressionTest {

    @Mock
    private ExpressionContext context;
    @Mock
    private Expression<Command> commandExpression1;
    @Mock
    private Command command1;
    @Mock
    private Expression<Command> commandExpression2;
    @Mock
    private Command command2;

    private Expression<Command> expression;

    @BeforeEach
    void setUp() {
        expression = new MacroCommandExpression(commandExpression1, commandExpression2);
    }

    @Test
    void interpret() {
        when(commandExpression1.interpret(context)).thenReturn(command1);
        when(commandExpression2.interpret(context)).thenReturn(command2);

        expression.interpret(context).execute();

        verify(commandExpression1, times(1)).interpret(context);
        verify(command1, times(1)).execute();
        verify(commandExpression2, times(1)).interpret(context);
        verify(command2, times(1)).execute();

    }
}