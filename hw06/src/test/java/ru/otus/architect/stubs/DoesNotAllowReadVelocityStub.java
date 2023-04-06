package ru.otus.architect.stubs;

import ru.otus.architect.exceptions.ReadVelocityException;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.game.objects.dimension.Coordinates;

public class DoesNotAllowReadVelocityStub implements Movable {

    private Coordinates position = new Coordinates(0, 0);

    @Override
    public Coordinates getPosition() {
        return position;
    }

    @Override
    public void setPosition(Coordinates newPosition) {
        this.position = newPosition;
    }

    @Override
    public Coordinates getVelocity() {
        throw new ReadVelocityException();
    }

    public void setVelocity(Coordinates velocity) {
    }
}
