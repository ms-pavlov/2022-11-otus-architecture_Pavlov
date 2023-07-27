package ru.otus.architect.exceptions.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.ExceptionHandlingStrategy;
import ru.otus.architect.stubs.GameStub;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoggingExceptionHandlerTest {

    @Mock
    Logger logger;

    @Mock
    Command command;

    @Mock
    ExceptionHandlingStrategy exceptionHandlingStrategy;

    private Queue<Command> commands;

    @BeforeEach
    void init() {
        commands = new ArrayDeque<>();
        commands.add(command);
    }

    @Test
    @DisplayName("Лог пуст, очередь команд пуста, если нет исключений")
    void testCaseWhenNoExceptions() {
        // arrange
        var cut = new GameStub(commands, exceptionHandlingStrategy);
        // act
        cut.run();
        // assert
        assertTrue(commands.isEmpty());
        verify(logger, never()).log(any(), anyString());
    }

    @Test
    @DisplayName("Исключение должно быть обработано хендлером и попасть в лог")
    void testExceptionLoggingHandler() {
        // arrange
        when(logger.getLevel()).thenReturn(Level.INFO);
        when(exceptionHandlingStrategy.getHandler(any(), any())).thenReturn(new LoggingExceptionHandler(logger));
        doThrow(new RuntimeException("message")).when(command).execute();
        var cut = new GameStub(commands, exceptionHandlingStrategy);
        //act
        cut.run();
        // assert
        verify(logger, times(1)).log(any(), any(Supplier.class));
        assertTrue(commands.isEmpty());
    }
}
