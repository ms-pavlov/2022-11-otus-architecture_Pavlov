package ru.otus.commands.strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.commands.Command;
import ru.otus.commands.CrossableCommandFactory;
import ru.otus.mesh.PlayingField;
import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrossCalculationStrategyTest {

    @Mock
    private CrossableCommandFactory crossableCommandFactory;
    @Mock
    private PlayingField field;
    @Mock
    private Surrounding surrounding;
    @Mock
    private Crossable crossable;
    @Mock
    private Command command;

    private BiFunction<PlayingField, Crossable, Command> strategy;

    @BeforeEach
    void setUp() {
        when(crossable.getSurrounding(field)).thenReturn(surrounding);
        strategy = new CrossCalculationStrategy(crossableCommandFactory);
    }

    @Test
    @DisplayName("Создает команты для проверки пересечений с другими объектами в области")
    void apply() {
        when(crossableCommandFactory.apply(crossable, surrounding)).thenReturn(List.of(command));

        strategy.apply(field, crossable).execute();

        verify(crossableCommandFactory, times(1)).apply(crossable, surrounding);
        verify(command, times(1)).execute();
    }
}