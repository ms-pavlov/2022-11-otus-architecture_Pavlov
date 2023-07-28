package ru.otus.architect.game.objects.dimension.vector;

import java.util.List;

public interface Vector {

    Vector vectorAdd(Vector vector);

    Vector scalarAdd(double acceleration);

    List<Integer> getCoordinates();

    int getDimension();
}
