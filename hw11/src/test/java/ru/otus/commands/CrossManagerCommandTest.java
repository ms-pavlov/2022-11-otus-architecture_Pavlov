package ru.otus.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.objects.Crossable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrossManagerCommandTest {

    @Mock
    private Crossable crossable1;
    @Mock
    private Crossable crossable2;
    @Mock
    private Command onCrossCommand;

    private Command command;

    @BeforeEach
    void setUp() {
        command = new CrossManagerCommand(crossable1, crossable2, onCrossCommand);
    }


    @Test
    @DisplayName("Если пересечения нет, то onCrossCommand не выполняется ")
    void executeNoCross() {
        when(crossable1.hasCrossing(crossable2)).thenReturn(false);

        command.execute();

        verify(onCrossCommand, never()).execute();
    }

    @Test
    @DisplayName("Если есть пересечения, то onCrossCommand выполняется ")
    void executeCross() {
        when(crossable1.hasCrossing(crossable2)).thenReturn(true);

        command.execute();

        verify(onCrossCommand, times(1)).execute();
    }
}