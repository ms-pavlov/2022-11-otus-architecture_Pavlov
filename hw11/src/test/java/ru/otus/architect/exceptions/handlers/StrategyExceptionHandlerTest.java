package ru.otus.architect.exceptions.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.ExceptionHandlingStrategy;
import ru.otus.architect.exceptions.RetriesStrategy;
import ru.otus.architect.stubs.CommandStub;
import ru.otus.architect.stubs.GameStub;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StrategyExceptionHandlerTest {

    @Mock
    Logger logger;

    @Spy
    CommandStub command;

    @Mock
    ExceptionHandlingStrategy exceptionHandlingStrategy;

    @Mock
    RetriesStrategy retriesStrategy;

    private Queue<Command> commands;

    @BeforeEach
    void init() {
        commands = new ArrayDeque<>();
        commands.add(command);
    }

    @Test
    @DisplayName("Очередь команд пуста, если нет исключений")
    void testCaseWhenNoExceptions() {
        // arrange
        var cut = new GameStub(commands, exceptionHandlingStrategy);
        // act
        cut.run();
        // assert
        assertTrue(commands.isEmpty());
    }

    @Test
    @DisplayName("Исключение должно быть обработано - 2 раза повтор, потом запись в лог")
    void testStrategyExceptionHandler() {
        // arrange

        var retries = new HashMap<Command, AtomicInteger>();
        retries.put(command, new AtomicInteger(0));
        when(retriesStrategy.getAllowedRetries(any())).thenReturn(2);
        var handler = new StrategyExceptionHandler(commands, retriesStrategy, retries, logger);
        when(exceptionHandlingStrategy.getHandler(any(), any())).thenReturn(handler);
        when(logger.getLevel()).thenReturn(Level.INFO);

        doThrow(new RuntimeException("message")).when(command).execute();
        var cut = new GameStub(commands, exceptionHandlingStrategy);
        //act
        cut.run();
        // assert
        verify(command, times(2)).execute();
        assertTrue(commands.isEmpty());
    }
}
