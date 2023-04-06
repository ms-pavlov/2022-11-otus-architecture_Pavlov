package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleComparatorImplTest {

    private final static Double DELTA = 0.0005;
    private final static Double LARGE_DELTA = 1.0000000000000004*DELTA;
    private final static Double LESS_DELTA = 1.0000000000000003*DELTA;
    private final static Double TEST_DOUBLE = 0.0005;
    private final static int COMPARATOR_EQUALS = 0;
    private final static int COMPARATOR_LARGE = 1;
    private final static int COMPARATOR_LESS = -1;

    private DoubleComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new DoubleComparatorImpl(DELTA);
    }


    @Test
    @DisplayName("Если сравниваемые числа различаются на величину меньше дельты, возвращает 0")
    void compareEquals() {
        var result = comparator.compare(TEST_DOUBLE, TEST_DOUBLE + LESS_DELTA);

        assertEquals(COMPARATOR_EQUALS, result);
    }

    @Test
    @DisplayName("Если первое число меньше второго на величину больше дельты, возвращает -1")
    void compareLess() {
        var result = comparator.compare(TEST_DOUBLE, TEST_DOUBLE + LARGE_DELTA);

        assertEquals(COMPARATOR_LESS, result);
    }

    @Test
    @DisplayName("Если первое число меньше второго на величину больше дельты, возвращает -1")
    void compareLarge() {
        var result = comparator.compare(TEST_DOUBLE, TEST_DOUBLE - LARGE_DELTA);

        assertEquals(COMPARATOR_LARGE, result);
    }

    @Test
    @DisplayName("Возвращает исключение если одино из чисел NaN")
    void compareNaN() {
        assertThrows(DoubleComparatorException.class, () -> comparator.compare(TEST_DOUBLE, Double.NaN));
        assertThrows(DoubleComparatorException.class, () -> comparator.compare(Double.NaN, TEST_DOUBLE));
        assertThrows(DoubleComparatorException.class, () -> comparator.compare(Double.NaN, Double.NaN));
    }

    @Test
    @DisplayName("Возвращает исключение если одино из чисел null")
    void compareNull() {
        assertThrows(DoubleComparatorException.class, () -> comparator.compare(TEST_DOUBLE, null));
        assertThrows(DoubleComparatorException.class, () -> comparator.compare(null, TEST_DOUBLE));
        assertThrows(DoubleComparatorException.class, () -> comparator.compare(null, null));
    }
}