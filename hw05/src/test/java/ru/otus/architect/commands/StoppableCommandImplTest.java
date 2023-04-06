package ru.otus.architect.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.architect.commands.stoppable.StoppableCommand;
import ru.otus.architect.commands.stoppable.StoppableCommandImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoppableCommandImplTest {
    private final static String TEST_MESSAGE_OK = "Ok";
    private final static String TEST_MESSAGE_FAILED = "FAILED";
    private List<String> log;

    @BeforeEach
    void setUp() {
        log = new ArrayList<>();
    }

    @Test
    @DisplayName("Команда выполняется")
    void execute() {
        StoppableCommand command = new StoppableCommandImpl(() -> log.add(TEST_MESSAGE_OK), () -> log.add(TEST_MESSAGE_FAILED));

        command.execute();

        assertEquals(1, log.size());

        assertEquals(TEST_MESSAGE_OK, log.get(0));
    }

    @Test
    @DisplayName("После stop выполняется terminationCommand")
    void stop() {
        StoppableCommand command = new StoppableCommandImpl(() -> log.add(TEST_MESSAGE_FAILED), () -> log.add(TEST_MESSAGE_OK));

        command.stop();
        command.execute();

        assertEquals(1, log.size());

        assertEquals(TEST_MESSAGE_OK, log.get(0));
    }
}