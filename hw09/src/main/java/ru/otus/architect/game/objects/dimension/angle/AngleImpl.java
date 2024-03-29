package ru.otus.architect.game.objects.dimension.angle;

public class AngleImpl implements Angle {
    private final static double DELTA = 0.0005;
    private final int discreteAngleCount;
    private final int corner;

    public AngleImpl(int corner, int discreteAngleCount) {
        if (discreteAngleCount <= 0) {
            throw new RuntimeException("Bad discreteAngleCount");
        }
        this.discreteAngleCount = discreteAngleCount;
        this.corner = corner;
    }

    @Override
    public double getAngle() {
        return (Math.PI * corner) / discreteAngleCount;
    }

    @Override
    public Angle add(Angle angle) {
        return new AngleImpl(corner + convertAngle(angle.getAngle()), discreteAngleCount);
    }

    private int convertAngle(double angle) {
        return (int) Math.round(discreteAngleCount * angle / Math.PI);
    }

}
