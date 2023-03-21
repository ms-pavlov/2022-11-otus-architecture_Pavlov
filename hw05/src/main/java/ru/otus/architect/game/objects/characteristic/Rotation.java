package ru.otus.architect.game.objects.characteristic;

import ru.otus.architect.game.objects.dimension.angle.Angle;

public interface Rotation {

    Angle getAngle();

    void setAngle(Angle angle);

    Angle getAngularVelocity();
}
