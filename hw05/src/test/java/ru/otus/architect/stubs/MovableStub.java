package ru.otus.architect.stubs;

import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.game.objects.dimension.Coordinates;

public class MovableStub implements Movable {

    private Coordinates position = new Coordinates(0, 0);
    private Coordinates velocity = new Coordinates(0, 0);


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
        return velocity;
    }

    public void setVelocity(Coordinates velocity) {
        this.velocity = velocity;
    }
}
