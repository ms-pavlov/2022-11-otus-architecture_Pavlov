package ru.otus.architect.game.objects.dimension;

import java.util.Objects;

public class Coordinates {
    private final static double PRECISION = Math.pow(10, -10);

    private double x;
    private double y;

    public Coordinates(double x, double y) {
        checkIfNumbers(x, y);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Coordinates plus(Coordinates other) {
        this.x += other.getX();
        this.y += other.getY();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(Math.abs(that.x - x), PRECISION) < 0 && Double.compare(Math.abs(that.y - y), PRECISION) < 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    private void checkIfNumbers(double x, double y) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("X must be a finite number");
        }

        if (Double.isNaN(y) || Double.isInfinite(y)) {
            throw new IllegalArgumentException("Y must be a finite number");
        }
    }
}
