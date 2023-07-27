package ru.otus.architect.game.objects.characteristic;

import ru.otus.architect.game.objects.dimension.vector.Vector;

public interface Accelerator {

    Vector getVelocity();

    void setVelocity(Vector velocity);

    double getAcceleration();
}
