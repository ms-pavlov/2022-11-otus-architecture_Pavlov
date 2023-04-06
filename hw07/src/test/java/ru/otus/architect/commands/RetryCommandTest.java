package ru.otus.architect.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class RetryCommandTest {

    @Mock
    Command command;

    private Queue<Command> commands;

    @BeforeEach
    void init() {
        commands = new ArrayDeque<>();
    }

    @AfterEach
    void destroy() {
        commands.clear();
    }

    @Test
    @DisplayName("Команда ставит переданную команду в очередь")
    void testExecute() {
        // arrange
        var cut = new RetryCommand(command, commands);
        // act
        cut.execute();
        // assert
        assertFalse(commands.isEmpty());
        assertEquals(command, commands.poll());
    }
}