package ru.otus.architect.commands.macro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.stoppable.StoppableCommand;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.game.objects.dimension.Coordinates;
import ru.otus.architect.game.objects.dimension.vector.Vector2DBuilder;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoppableMoveMacroCommandTest {

    private final static int STEPS = 5;

    private final static Coordinates VELOCITY = new Coordinates(1, 0);
    private final static Coordinates RESULT = new Coordinates(6, 1);

    @Mock
    private Movable movable;
    @Mock
    private Command terminateCommand;

    private Coordinates coordinates = new Coordinates(1, 1);
    private Queue<Command> queue;
    private StoppableCommand command;

    @BeforeEach
    void setUp() {
        queue = new LinkedList<>();
        command = new StoppableMoveMacroCommand(movable, STEPS, queue, terminateCommand);
    }


    @Test
    void execute() {
        when(movable.getPosition()).thenReturn(coordinates);
        when(movable.getVelocity()).thenReturn(VELOCITY);
        doAnswer(invocationOnMock -> coordinates = invocationOnMock.getArgument(0))
                .when(movable).setPosition(any());
        queue.offer(command);

        while (!queue.isEmpty()) {
            queue.poll().execute();
        }

        assertEquals(RESULT, coordinates);
        verify(movable, times(STEPS)).setPosition(any());
        verify(terminateCommand, times(1)).execute();
    }

    @Test
    void stop() {
        queue.offer(command);
        command.stop();

        while (!queue.isEmpty()) {
            queue.poll().execute();
        }

        verify(movable, times(0)).setPosition(any());
        verify(terminateCommand, times(1)).execute();
    }
}