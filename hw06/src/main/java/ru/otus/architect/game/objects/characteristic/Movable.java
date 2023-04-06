package ru.otus.architect.game.objects.characteristic;

import ru.otus.architect.game.StarShip;
import ru.otus.architect.game.objects.dimension.Coordinates;
import ru.otus.architect.processor.IoCAdapterPlugin;

@IoCAdapterPlugin(classes = {StarShip.class})
public interface Movable {
    Coordinates getPosition();

    void setPosition(Coordinates newPosition);

    Coordinates getVelocity();
}
