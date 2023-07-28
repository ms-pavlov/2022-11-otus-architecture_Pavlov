package ru.otus.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mesh.PlayingField;
import ru.otus.objects.Crossable;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChainCrossCommandTest {

    @Mock
    private PlayingField field;
    @Mock
    private Crossable crossable;
    @Mock
    private BiFunction<PlayingField, Crossable, Command> crossCalculationStrategy;
    @Mock
    private BiFunction<PlayingField, Crossable, Command> currentSurroundingStrategy;
    @Mock
    private Command crossCalculation;
    @Mock
    private Command currentSurrounding;


    private Command command;

    @BeforeEach
    void setUp() {
        when(crossCalculationStrategy.apply(field, crossable)).thenReturn(crossCalculation);
        when(currentSurroundingStrategy.apply(field, crossable)).thenReturn(currentSurrounding);

        command = new ChainCrossCommand(field, crossable, crossCalculationStrategy, currentSurroundingStrategy);
    }

    @Test
    @DisplayName("При выполнении выполни выполняет команды в нужном порядке")
    void execute() {
        command.execute();

        var order = inOrder(currentSurrounding, crossCalculation);

        order.verify(currentSurrounding, times(1)).execute();
        order.verify(crossCalculation, times(1)).execute();

    }
}