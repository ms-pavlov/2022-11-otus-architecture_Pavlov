package ru.otus.architect.game.objects.dimension.vector;

import java.util.Map;

public class VectorDecorator {

    private final Vector vector;

    public VectorDecorator(Vector vector) {
        this.vector = vector;
    }

    public Map<String, String> getCoordinatesAsString() {
        var coordinates = vector.getCoordinates();
        if (coordinates.size() > 1) {
            return Map.of(
                    "coordinate.x", coordinates.get(0).toString(),
                    "coordinate.y", coordinates.get(1).toString());
        }
        return Map.of("coordinates", coordinates.toString());
    }
}
