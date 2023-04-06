package ru.otus.architect.game.objects.dimension.angle;

public interface Angle {

    double getAngle();

    Angle add(Angle angularVelocity);
}
