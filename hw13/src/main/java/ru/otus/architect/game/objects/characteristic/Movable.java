package ru.otus.architect.game.objects.characteristic;

import ru.otus.architect.game.objects.dimension.vector.Vector;

public interface Movable {
    Vector getPosition();

    void setPosition(Vector newPosition);

    Vector getVelocity();
}
