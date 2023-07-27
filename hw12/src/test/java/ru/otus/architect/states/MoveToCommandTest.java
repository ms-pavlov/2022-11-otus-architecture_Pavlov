package ru.otus.architect.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;

import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class MoveToCommandTest {

    @Mock
    private LoopState state;
    @Mock
    private QueueLoop queueLoop;
    @Mock
    private BlockingQueue<Command> commands;

    private Command command;

    @BeforeEach
    void setUp() {
        command = new MoveToCommand(queueLoop, commands);
    }

    @Test
    @DisplayName("Выполняет moveTo()")
    void execute() {
        when(queueLoop.getState()).thenReturn(state);

        command.execute();

        verify(state, times(1)).moveTo(commands);
    }
}