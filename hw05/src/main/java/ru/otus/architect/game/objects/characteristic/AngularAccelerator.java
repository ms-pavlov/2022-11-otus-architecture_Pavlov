package ru.otus.architect.game.objects.characteristic;

import ru.otus.architect.game.objects.dimension.angle.Angle;

public interface AngularAccelerator {

    Angle getAngularVelocity();

    void setAngularVelocity(Angle angle);

    Angle getAngularAcceleration();
}
