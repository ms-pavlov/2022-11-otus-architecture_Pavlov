package ru.otus.architect.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.game.objects.dimension.vector.Vector2DBuilder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoveCommandTest {

    @Mock
    private Movable movable;

    private Command command;

    @BeforeEach
    void setUp() {
        command = new MoveCommand(movable);
    }

    @Test
    @DisplayName("Объект с координатами (12,5) и скоростью (-7, 3) перемещается одной командой на позицию (5, 8)")
    void testMoveFromOnePointToAnother() {
        // arrange
        when(movable.getPosition()).thenReturn(Vector2DBuilder.builder().x(12).y(5).build());
        when(movable.getVelocity()).thenReturn(Vector2DBuilder.builder().x(-7).y(3).build());
        // act
        command.execute();
        // assert


        verify(movable, times(1))
                .setPosition(
                        Vector2DBuilder.builder().x(5).y(8).build());
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно прочитать положение в пространстве, приводит к ошибке")
    void testThatMovableAllowsReadItsPosition() {
        doThrow(new RuntimeException()).when(movable).getPosition();
        // act/assert
        assertThrows(RuntimeException.class, command::execute);
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно прочитать значение мгновенной скорости, приводит к ошибке")
    void testThatMovableAllowsReadItsVelocity() {
        doThrow(new RuntimeException()).when(movable).getVelocity();
        // act/assert
        assertThrows(RuntimeException.class, command::execute);
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно изменить положение в пространстве, приводит к ошибке")
    void testThatMovableMovesInSpace() {
        doThrow(new RuntimeException()).when(movable).setPosition(any());
        when(movable.getPosition()).thenReturn(Vector2DBuilder.builder().x(12).y(5).build());
        when(movable.getVelocity()).thenReturn(Vector2DBuilder.builder().x(-7).y(3).build());
        // act/assert
        assertThrows(RuntimeException.class, command::execute);
    }
}
