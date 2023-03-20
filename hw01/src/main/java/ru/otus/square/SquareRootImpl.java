package ru.otus.square;

import ru.otus.DoubleComparator;

import java.util.List;

public class SquareRootImpl implements SquareRoot {
    private final DoubleComparator comparator;

    public SquareRootImpl(DoubleComparator comparator) {
        this.comparator = comparator;
    }

    @Override
    public List<Double> solve(double a, double b, double c) {
        checkParam(a, b, c);

        double d = b * b - 4 * a * c;
        checkDiscriminant(d);
        int comp = comparator.compare(d, 0.0);

        if (comp < 0) {
            return List.of();
        }
        if (comp > 0) {
            return List.of(
                    (-b - Math.sqrt(d)) / (2 * a),
                    (-b + Math.sqrt(d)) / (2 * a)
            );
        }
        return List.of(
                (-b) / (2 * a)
        );
    }

    private void checkDiscriminant(double d) {
        if (Double.isInfinite(d)) {
            throw new RuntimeException("Discriminant is infinite");
        }
    }

    private void checkParam(double a, double b, double c) {
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c)) {
            throw new RuntimeException("Params is NaN");
        }
        if (Double.isInfinite(a) || Double.isInfinite(b) || Double.isInfinite(c)) {
            throw new RuntimeException("Params is Infinite");
        }
        if (comparator.compare(0.0, a) == 0) {
            throw new RuntimeException("Not Quadratic equation");
        }
    }
}
