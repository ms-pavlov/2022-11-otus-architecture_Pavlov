package ru.otus.architect.game.objects.dimension.vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VectorImpl implements Vector {

    private final List<Integer> coordinates;

    public VectorImpl(Integer... coordinates) {
        this.coordinates = List.of(coordinates);
    }

    public VectorImpl(List<Integer> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Vector vectorAdd(Vector vector) {
        if (vector.getDimension() != this.getDimension()) {
            throw new VectorsDimensionException();
        }
        return new VectorImpl(addCoordinates(vector.getCoordinates()));
    }

    @Override
    public Vector scalarAdd(double acceleration) {
        var length = Math.sqrt(coordinates.stream().mapToInt(value -> value*value).sum());
        var multiplier = 1.0 + acceleration / length;
        return new VectorImpl(coordinates.stream().map(value -> (int) Math.round(value*multiplier)).collect(Collectors.toList()));
    }

    @Override
    public List<Integer> getCoordinates() {
        return new ArrayList<>(coordinates);
    }

    @Override
    public int getDimension() {
        return coordinates.size();
    }

    private List<Integer> addCoordinates(List<Integer> velocity) {
        List<Integer> result = new ArrayList<>();
        for (var i = 0; i < coordinates.size(); i++) {
            result.add(i, coordinates.get(i) + velocity.get(i));
        }
        return result;
    }
}
