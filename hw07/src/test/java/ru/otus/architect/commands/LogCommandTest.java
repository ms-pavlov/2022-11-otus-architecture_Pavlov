package ru.otus.architect.commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogCommandTest {

    private static final String ERROR_MSG = "error";

    @Mock
    Logger logger;

    @Test
    @DisplayName("Команда пишет в лог")
    void testExecute() {
        // arrange
        when(logger.getLevel()).thenReturn(Level.INFO);
        Supplier<String> supplier = () -> ERROR_MSG;
        var cut = new LogCommand(supplier, logger);
        // act
        cut.execute();
        // assert
        verify(logger, times(1)).log(Level.INFO, supplier);
    }
}