package ru.otus.architect.game.objects.characteristic;

import ru.otus.architect.game.objects.dimension.Coordinates;

public interface Movable {
    Coordinates getPosition();

    void setPosition(Coordinates newPosition);

    Coordinates getVelocity();
}
