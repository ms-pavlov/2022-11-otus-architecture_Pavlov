package ru.otus.architect.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.api.AnswerConsumer;
import ru.otus.openapi.model.Message;

import java.util.Map;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandFactoryImplTest {

    private final static String COMMAND_NAME = "name";

    @Mock
    private Map<String, BiFunction<Message, AnswerConsumer, Command>> commandBuilderStorage;
    @Mock
    private Command command;
    @Mock
    private Message message;
    @Mock
    private AnswerConsumer answerConsumer;

    private CommandFactory commandFactory;

    @BeforeEach
    void setUp() {
        commandFactory = new CommandFactoryImpl(commandBuilderStorage);
    }

    @Test
    @DisplayName("CommandFactory создает зарегистрированную команду")
    void create() {
        when(commandBuilderStorage.get(COMMAND_NAME)).thenReturn((message1, answerConsumer1) -> command);

        var result = commandFactory.create(COMMAND_NAME, message, answerConsumer);

        assertEquals(command, result);
    }

    @Test
    @DisplayName("CommandFactory создает зарегистрированную команду")
    void createMissingCommand() {
        assertThrows(RuntimeException.class, () -> commandFactory.create(COMMAND_NAME, message, answerConsumer));
    }
}