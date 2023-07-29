package ru.otus.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.commands.strategies.ChangeSurroundingStrategy;
import ru.otus.mesh.PlayingField;
import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrentSurroundingCommandTest {

    @Mock
    private PlayingField field;
    @Mock
    private Surrounding surrounding;
    @Mock
    private Surrounding otherSurrounding;
    @Mock
    private Crossable crossable;
    @Mock
    private ChangeSurroundingStrategy changeSurroundingStrategy;

    private Command command;

    @BeforeEach
    void setUp() {
        when(field.getSurroundings()).thenReturn(List.of(surrounding));
        command = new CurrentSurroundingCommand(field, crossable, changeSurroundingStrategy);
    }

    @Test
    @DisplayName("Если объект не переместился в другую окресность, ничего не происходит")
    void executeNoSurroundingChange() {
        when(crossable.getSurrounding(field)).thenReturn(surrounding);
        when(surrounding.contains(crossable)).thenReturn(true);

        command.execute();

        verify(changeSurroundingStrategy, never()).accept(eq(crossable), any());
    }

    @Test
    @DisplayName("Если объект  переместился в другую окресность, выполняется соответствующая стратегия")
    void executeSurroundingChange() {
        when(crossable.getSurrounding(field)).thenReturn(otherSurrounding);
        when(surrounding.contains(crossable)).thenReturn(true);

        command.execute();

        verify(changeSurroundingStrategy, times(1)).accept(eq(crossable), any());
    }
}