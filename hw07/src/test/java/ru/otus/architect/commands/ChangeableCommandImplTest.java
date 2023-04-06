package ru.otus.architect.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.architect.commands.stoppable.ChangeableCommand;
import ru.otus.architect.commands.stoppable.ChangeableCommandImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeableCommandImplTest {
    private final static String TEST_MESSAGE_OK = "Ok";
    private final static String TEST_MESSAGE_FAILED = "FAILED";
    private List<String> log;

    @BeforeEach
    void setUp() {
        log = new ArrayList<>();
    }

    @Test
    @DisplayName("После изменения команды будут выполнены новые действия")
    void changeCommand() {
        ChangeableCommand command = new ChangeableCommandImpl(() -> log.add(TEST_MESSAGE_FAILED));

        command.changeCommand(() -> log.add(TEST_MESSAGE_OK));

        command.execute();

        assertEquals(1, log.size());

        assertEquals(TEST_MESSAGE_OK, log.get(0));
    }

    @Test
    @DisplayName("Команда выполняется")
    void execute() {
        ChangeableCommand command = new ChangeableCommandImpl(() -> log.add(TEST_MESSAGE_OK));

        command.execute();

        assertEquals(1, log.size());

        assertEquals(TEST_MESSAGE_OK, log.get(0));

    }
}