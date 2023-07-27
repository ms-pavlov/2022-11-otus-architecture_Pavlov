package ru.otus.architect.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class HardStopStateCommandTest {

    @Mock
    private LoopState state;
    @Mock
    private QueueLoop queueLoop;

    private Command command;

    @BeforeEach
    void setUp() {
        command = new HardStopStateCommand(queueLoop);
    }

    @Test
    @DisplayName("Выполняет stop()")
    void execute() {
        when(queueLoop.getState()).thenReturn(state);

        command.execute();

        verify(state, times(1)).stop();
    }
}