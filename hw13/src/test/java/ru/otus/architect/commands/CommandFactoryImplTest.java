package ru.otus.architect.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.api.AnswerConsumer;
import ru.otus.architect.expressions.ExpressionContext;
import ru.otus.architect.expressions.ExpressionContextFactory;
import ru.otus.openapi.model.Message;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandFactoryImplTest {

    private final static String COMMAND_NAME = "name";

    @Mock
    private Map<String, Function<ExpressionContext, Command>> commandBuilderStorage;
    @Mock
    private Command command;
    @Mock
    private Message message;
    @Mock
    private AnswerConsumer answerConsumer;
    @Mock
    private ExpressionContextFactory expressionContextFactory;
    @Mock
    private ExpressionContext expressionContext;
    private CommandFactory commandFactory;

    @BeforeEach
    void setUp() {
        commandFactory = new CommandFactoryImpl(commandBuilderStorage, expressionContextFactory);
    }

    @Test
    @DisplayName("CommandFactory создает зарегистрированную команду")
    void create() {
        when(expressionContextFactory.create(message, answerConsumer)).thenReturn(expressionContext);
        when(commandBuilderStorage.get(COMMAND_NAME)).thenReturn(expressionContext -> command);

        var result = commandFactory.create(COMMAND_NAME, message, answerConsumer);

        assertEquals(command, result);
        verify(commandBuilderStorage, times(1)).get(COMMAND_NAME);
        verify(expressionContextFactory, times(1)).create(message, answerConsumer);
    }

    @Test
    @DisplayName("CommandFactory создает зарегистрированную команду")
    void createMissingCommand() {
        assertThrows(RuntimeException.class, () -> commandFactory.create(COMMAND_NAME, message, answerConsumer));
    }
}