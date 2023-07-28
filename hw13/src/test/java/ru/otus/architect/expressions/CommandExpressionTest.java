package ru.otus.architect.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.api.AnswerConsumer;
import ru.otus.architect.commands.Command;
import ru.otus.openapi.model.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandExpressionTest {

    private final static String ACTION_NAME = "test";

    @Mock
    private ExpressionContext context;
    @Mock
    private Message message;
    @Mock
    private AnswerConsumer answerConsumer;
    @Mock
    private Expression<String> stringExpression;
    @Mock
    private Command command;

    private Expression<Command> commandExpression;

    @BeforeEach
    void setUp() {
        commandExpression = new CommandExpression(stringExpression);
    }

    @Test
    void interpret() {
        when(context.getAnswerConsumer()).thenReturn(answerConsumer);
        when(context.getMessage()).thenReturn(message);
        when(stringExpression.interpret(context)).thenReturn(ACTION_NAME);
        when(
                context.resolve(
                        ACTION_NAME,
                        message,
                        answerConsumer))
                .thenReturn(command);

        var result = commandExpression.interpret(context);

        assertEquals(command, result);
        verify(context, times(1))
                .resolve(
                        ACTION_NAME,
                        message,
                        answerConsumer);

    }
}