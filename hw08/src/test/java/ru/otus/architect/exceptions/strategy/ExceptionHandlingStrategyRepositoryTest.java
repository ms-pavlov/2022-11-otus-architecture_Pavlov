package ru.otus.architect.exceptions.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.LogCommand;
import ru.otus.architect.commands.MoveCommand;
import ru.otus.architect.exceptions.ExceptionHandler;
import ru.otus.architect.exceptions.ExceptionHandlingStrategy;


import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlingStrategyRepositoryTest {

    @Mock
    private Command command;
    @Mock
    private ExceptionHandler exceptionHandler;


    private static class ExceptionHandlerStub implements ExceptionHandler {

        @Override
        public void handle(Command cmd, Exception ex) {
            // DO NOTHING
        }
    }

    ExceptionHandlerStub defaultExceptionHandler;

    ExceptionHandlingStrategy strategy;

    @BeforeEach
    void init() {
        Map<Class<? extends Command>, Map<Class<? extends Exception>, ExceptionHandler>> handlers =
                new HashMap<>();
        defaultExceptionHandler = new ExceptionHandlerStub();
        strategy = new ExceptionHandlingStrategyRepository(
                handlers,
                defaultExceptionHandler
        );
    }

    @Test
    @DisplayName("Стратегия должна успешно регистрироваться на команду и исключение и вернуть нужный хендлер")
    void registerStrategyCommandExceptionSuccess() {
        // act
        strategy.registerStrategy(Command.class, RuntimeException.class, exceptionHandler);
        // assert
        assertEquals(exceptionHandler, strategy.getHandler(Command.class, RuntimeException.class));
    }

    @Test
    @DisplayName("Стратегия должна успешно регистрироваться на только команду и вернуть нужный хендлер")
    void registerStrategyCommandSuccess() {
        // act
        strategy.registerStrategyOnCommand(Command.class, exceptionHandler);
        // assert
        assertEquals(exceptionHandler, strategy.getHandler(Command.class, RuntimeException.class));
    }

    @Test
    @DisplayName("Стратегия должна успешно регистрироваться на только команду и не найдя, вернуть дефолтный хендлер")
    void registerStrategyCommandFailAndReturnDefault() {
        // act
        strategy.registerStrategyOnCommand(LogCommand.class, exceptionHandler);
        // assert
        assertNotEquals(exceptionHandler, strategy.getHandler(MoveCommand.class, RuntimeException.class));
        assertEquals(defaultExceptionHandler, strategy.getHandler(MoveCommand.class, RuntimeException.class));
    }

    @Test
    @DisplayName("Стратегия должна успешно регистрироваться на только исключение и вернуть нужный хендлер")
    void registerStrategyExceptionSuccess() {
        // act
        strategy.registerStrategyOnException(NoSuchElementException.class, exceptionHandler);
        // assert
        assertEquals(exceptionHandler, strategy.getHandler(Command.class, NoSuchElementException.class));
    }

    @Test
    @DisplayName("Стратегия должна успешно регистрироваться на только исключение и не найдя хендлер вернуть дефолтный")
    void registerStrategyExceptionFailAndDefault() {
        // act
        strategy.registerStrategyOnException(RuntimeException.class, exceptionHandler);
        // assert
        assertNotEquals(exceptionHandler, strategy.getHandler(Command.class, NoSuchElementException.class));
        assertEquals(defaultExceptionHandler, strategy.getHandler(Command.class, NoSuchElementException.class));
    }

    @Test
    @DisplayName("Стратегия должна вернуть дефолтный хендлер если не найден подходящий")
    void getDefaultHandlerIfNotFoundOne() {
        assertEquals(defaultExceptionHandler, strategy.getHandler(Command.class, RuntimeException.class));
    }
}