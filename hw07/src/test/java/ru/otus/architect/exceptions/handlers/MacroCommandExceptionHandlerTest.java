package ru.otus.architect.exceptions.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.CommandException;

import java.util.ArrayDeque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MacroCommandExceptionHandlerTest {

    @Mock
    Command completingCommand;

    @Mock
    Command throwingExceptionCommand;


    @Test
    @DisplayName("Исключение должно быть обработано и макрокоманда должна остановиться")
    void testExceptionLoggingHandler() {
        // arrange
        var commandsQueue = new ArrayDeque<>(List.of(completingCommand, throwingExceptionCommand));
        var cut = new MacroCommandExceptionHandler(commandsQueue);
        //act and assert
        assertThrows(CommandException.class, () -> cut.handle(throwingExceptionCommand, new RuntimeException()));
        assertFalse(commandsQueue.isEmpty());
    }
}