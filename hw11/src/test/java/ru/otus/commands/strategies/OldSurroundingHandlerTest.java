package ru.otus.commands.strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mesh.PlayingField;
import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OldSurroundingHandlerTest {

    @Mock
    private PlayingField field;
    @Mock
    private Crossable crossable;
    @Mock
    private Surrounding surrounding;

    private Consumer<Crossable> consumer;

    @BeforeEach
    void setUp() {
        when(crossable.getSurrounding(field)).thenReturn(surrounding);

        consumer = new OldSurroundingHandler(field);
    }

    @Test
    @DisplayName("Удаляет объект из области")
    void accept() {
        consumer.accept(crossable);

        verify(surrounding, times(1)).remove(crossable);
    }
}