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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetryingExceptionHandlerTest {
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
    @DisplayName("Исключение должно быть обработано и команда должна снова попасть в очередь")
    void testExceptionLoggingHandler() {
        // arrange
        when(exceptionHandlingStrategy.getHandler(any(), any())).thenReturn(new RetryingExceptionHandler(commands));
        doThrow(new RuntimeException("message"))
                .doNothing()
                .when(command).execute();
        var cut = new GameStub(commands, exceptionHandlingStrategy);
        //act
        cut.run();
        // assert
        verify(command, times(2)).execute();
        assertTrue(commands.isEmpty());
    }
}