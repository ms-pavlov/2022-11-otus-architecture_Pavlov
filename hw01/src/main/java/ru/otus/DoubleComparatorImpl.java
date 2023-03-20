package ru.otus;

public class DoubleComparatorImpl implements DoubleComparator{
    private final double delta;

    public DoubleComparatorImpl(double delta) {
        this.delta = delta;
    }

    public int compare(Double o1, Double o2) {
        if(null == o1 || null == o2) {
            throw new DoubleComparatorException();
        }
        if(Double.isNaN(o1) || Double.isNaN(o2)) {
            throw new DoubleComparatorException();
        }
        if(Math.abs(o1 - o2) <= delta) {
            return 0;
        }
        if (o1 - o2 < -delta) {
            return -1;
        }
        if (o1 - o2 > delta) {
            return 1;
        }
        throw new DoubleComparatorException();
    }

}
