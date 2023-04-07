package ru.otus.architect.commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.architect.game.objects.dimension.Coordinates;
import ru.otus.architect.exceptions.ReadPositionException;
import ru.otus.architect.exceptions.ReadVelocityException;
import ru.otus.architect.exceptions.WritePositionException;
import ru.otus.architect.stubs.DoesNotAllowReadPositionStub;
import ru.otus.architect.stubs.DoesNotAllowReadVelocityStub;
import ru.otus.architect.stubs.DoesNotAllowWritePositionStub;
import ru.otus.architect.stubs.MovableStub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoveCommandTest {

    @Test
    @DisplayName("Объект с координатами (12,5) и скоростью (-7, 3) перемещается одной командой на позицию (5, 8)")
    void testMoveFromOnePointToAnother() {
        // arrange
        var stub = new MovableStub();
        stub.setPosition(new Coordinates(12, 5));
        stub.setVelocity(new Coordinates(-7, 3));
        MoveCommand cut = new MoveCommand(stub);
        // act
        cut.execute();
        // assert
        assertEquals(stub.getPosition().getX(), 5);
        assertEquals(stub.getPosition().getY(), 8);
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно прочитать положение в пространстве, приводит к ошибке")
    void testThatMovableAllowsReadItsPosition() {
        var stub = new DoesNotAllowReadPositionStub();
        stub.setPosition(new Coordinates(12, 5));
        stub.setVelocity(new Coordinates(-7, 3));
        MoveCommand cut = new MoveCommand(stub);
        // act/assert
        assertThrows(ReadPositionException.class, cut::execute);
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно прочитать значение мгновенной скорости, приводит к ошибке")
    void testThatMovableAllowsReadItsVelocity() {
        var stub = new DoesNotAllowReadVelocityStub();
        stub.setPosition(new Coordinates(12, 5));
        stub.setVelocity(new Coordinates(-7, 3));
        MoveCommand cut = new MoveCommand(stub);
        // act/assert
        assertThrows(ReadVelocityException.class, cut::execute);
    }

    @Test
    @DisplayName("Попытка сдвинуть объект, у которого невозможно изменить положение в пространстве, приводит к ошибке")
    void testThatMovableMovesInSpace() {
        var stub = new DoesNotAllowWritePositionStub();
        // arrange
        stub.setVelocity(new Coordinates(-7, 3));
        MoveCommand cut = new MoveCommand(stub);
        // act/assert
        assertThrows(WritePositionException.class, cut::execute);
    }
}
