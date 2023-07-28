package ru.otus.architect.game.objects.dimension.vector;

import ru.otus.architect.game.objects.dimension.angle.AngleImpl;

public class Vector2DBuilder {
    private final static int DISCRETE_ANGLE_COUNT = 180;
    private int x;
    private int y;

    public Vector2DBuilder() {
    }

    public static Vector2DBuilder builder() {
        return new Vector2DBuilder();
    }

    public Vector2DBuilder x(int x) {
        this.x = x;
        return this;
    }

    public Vector2DBuilder y(int y) {
        this.y = y;
        return this;
    }

    public Vector build() {
        double radial = Math.sqrt(x * x + y * y);
        int corner;
        if (0 == x) {
            if (0 == y) {
                return new VectorImpl(x, y);
            }
            corner = (y > 0) ? DISCRETE_ANGLE_COUNT / 2 : 3 * DISCRETE_ANGLE_COUNT / 2;
        } else {
            corner = (int) Math.round(DISCRETE_ANGLE_COUNT * Math.atan(1.0 * y / x) / Math.PI);
            if (x < 0) {
                corner += DISCRETE_ANGLE_COUNT;
            }
        }
        return new PolarVector2D(radial, new AngleImpl(corner, DISCRETE_ANGLE_COUNT));
    }
}
