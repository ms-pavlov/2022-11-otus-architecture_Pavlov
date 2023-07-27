package ru.otus.architect.exceptions.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.LogCommand;
import ru.otus.architect.commands.MoveCommand;
import ru.otus.architect.exceptions.RetriesStrategy;
import ru.otus.architect.stubs.CommandStub;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RetriesStrategyRepositoryTest {

    RetriesStrategy strategy;

    @BeforeEach
    void init() {
        var initialValues = new HashMap<Class<? extends Command>, Integer>();
        initialValues.put(CommandStub.class, 1);
        strategy = new RetriesStrategyRepository(initialValues);
    }

    @Test
    @DisplayName("Стратегия должна успешно регистрироваться на команду и возвращать количество попыток")
    void registerStrategySuccess() {
        strategy.registerStrategy(LogCommand.class, 5);
        // assert
        assertEquals(5, strategy.getAllowedRetries(LogCommand.class));
    }

    @Test
    @DisplayName("Стратегия должна вернуть дефолтное количество попыток (1) если не найдена команда")
    void getDefaultHandlerIfNotFoundOne() {
        assertEquals(1, strategy.getAllowedRetries(MoveCommand.class));
    }

}