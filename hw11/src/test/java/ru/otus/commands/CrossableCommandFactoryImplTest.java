package ru.otus.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrossableCommandFactoryImplTest {

    @Mock
    private BiFunction<Crossable, Crossable, Command> crossableCommand;
    @Mock
    private Crossable crossable;
    @Mock
    private Crossable otherCrossable;
    @Mock
    private Surrounding surrounding;
    @Mock
    private Command command;

    private CrossableCommandFactory crossableCommandFactory;

    @BeforeEach
    void setUp() {
        crossableCommandFactory = new CrossableCommandFactoryImpl(crossableCommand);
    }

    @Test
    @DisplayName("Создает команду проверки пересечения с другими объектами в области")
    void apply() {
        when(crossableCommand.apply(crossable, otherCrossable)).thenReturn(command);
        when(surrounding.getAll()).thenReturn(List.of(crossable, otherCrossable));

        var result = crossableCommandFactory.apply(crossable, surrounding);

        assertTrue(result.contains(command));
        assertEquals(1, result.size());

        verify(crossableCommand, times(1)).apply(crossable, otherCrossable);
        verify(crossableCommand, never()).apply(crossable, crossable);
    }
}