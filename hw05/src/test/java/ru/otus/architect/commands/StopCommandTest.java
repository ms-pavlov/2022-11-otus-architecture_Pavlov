package ru.otus.architect.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.stoppable.StopCommand;
import ru.otus.architect.commands.stoppable.StoppableCommand;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StopCommandTest {

    @Mock
    private StoppableCommand command;

    private Command stopCommand;

    @BeforeEach
    void setUp() {
        stopCommand = new StopCommand(command);
    }

    @Test
    void execute() {
        stopCommand.execute();

        verify(command, times(1)).stop();
    }
}