package ru.otus.commands.strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.commands.Command;
import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChangeSurroundingStrategyImplTest {

    @Mock
    public Consumer<Crossable> oldSurroundingHandler;
    @Mock
    public BiConsumer<Crossable, Surrounding> newSurroundingHandler;
    @Mock
    public Crossable crossable;
    @Mock
    public Surrounding surrounding;

    public ChangeSurroundingStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ChangeSurroundingStrategyImpl(oldSurroundingHandler, newSurroundingHandler);
    }

    @Test
    @DisplayName("Выполняет стратегию для старой обласи и для новой")
    void accept() {
        strategy.accept(crossable, surrounding);

        verify(oldSurroundingHandler, times(1)).accept(crossable);
        verify(newSurroundingHandler, times(1)).accept(crossable, surrounding);
    }
}