package ru.otus.commands.strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.otus.commands.Command;
import ru.otus.commands.stoppable.ChangeableCommand;
import ru.otus.mesh.PlayingField;
import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrentSurroundingStrategyTest {

    @Mock
    private Command onCrossCommand;
    @Mock
    private ChangeableCommand changeableCommand;
    @Mock
    private PlayingField playingField;
    @Mock
    private Surrounding surrounding;
    @Mock
    private Surrounding otherSurrounding;
    @Mock
    private Crossable crossable;
    @Mock
    private Crossable otherCrossable;

    private BiFunction<PlayingField, Crossable, Command> strategy;

    @BeforeEach
    void setUp() {
        when(playingField.getSurroundings()).thenReturn(List.of(surrounding, otherSurrounding));
        strategy = new CurrentSurroundingStrategy(onCrossCommand, changeableCommand);
    }

    @Test
    void applyHasChange() {
        doAnswer((Answer<Void>) invocationOnMock -> {
            ((Command)invocationOnMock.getArgument(0)).execute();
            return null;
        }). when(changeableCommand).changeCommand(any());
        when(otherSurrounding.contains(crossable)).thenReturn(true);
        when(surrounding.contains(crossable)).thenReturn(false);
        when(otherSurrounding.getAll()).thenReturn(List.of(crossable, otherCrossable));
        when(crossable.getSurrounding(playingField)).thenReturn(surrounding);
        when(crossable.hasCrossing(otherCrossable)).thenReturn(true);

        strategy.apply(playingField, crossable).execute();

        verify(changeableCommand, times(1)).changeCommand(any());
        verify(onCrossCommand, times(1)).execute();
    }

}