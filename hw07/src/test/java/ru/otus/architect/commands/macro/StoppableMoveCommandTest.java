package ru.otus.architect.commands.macro;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.architect.game.objects.dimension.Coordinates;
import ru.otus.architect.stubs.MovableStub;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoppableMoveCommandTest {

    @Test
    @DisplayName("Объект с координатами (12,5) и скоростью (-1, 1) перемещается двумя командами на (10, 3)")
    void testMoveFromOnePointToAnother() {
        // arrange
        var stub = new MovableStub();
        stub.setPosition(new Coordinates(12, 5));
        stub.setVelocity(new Coordinates(-1, -1));
        StoppableMoveCommand cut = new StoppableMoveCommand(stub);
        // act
        cut.execute();
        cut.execute();
        // assert
        assertEquals(stub.getPosition().getX(), 10);
        assertEquals(stub.getPosition().getY(), 3);
    }

    @Test
    @DisplayName("Объект с координатами (12,5) и скоростью (-1, 1) перемещается только первой командой")
    void testMoveFromOnePointToAnotherWithStop() {
        // arrange
        var stub = new MovableStub();
        stub.setPosition(new Coordinates(12, 5));
        stub.setVelocity(new Coordinates(-1, -1));
        StoppableMoveCommand cut = new StoppableMoveCommand(stub);
        // act
        cut.execute();
        cut.stop();
        cut.execute();
        // assert
        assertEquals(stub.getPosition().getX(), 11);
        assertEquals(stub.getPosition().getY(), 4);
    }
}
