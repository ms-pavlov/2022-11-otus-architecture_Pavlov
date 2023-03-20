package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.square.SquareRoot;
import ru.otus.square.SquareRootImpl;

import static org.junit.jupiter.api.Assertions.*;

public class SquareRootTest {
    private final static Double DELTA = 0.000005;
    private final static Double ZERO = 0.2*DELTA;
    private final static Double NOR_ZERO = 1.0000000000000002*DELTA;
    private SquareRoot squareRoot;
    private DoubleComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new DoubleComparatorImpl(DELTA);
        squareRoot = new SquareRootImpl(comparator);
    }

    @Test
    @DisplayName("x^2 + 1 не имеет корней")
    void getRootNoRoot() {
        var result = squareRoot.getRoot(1.0, 0, 1.0);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("x^2 - 1 не имеет два корня")
    void getRootTwoRoot() {
        var result = squareRoot.getRoot(1.0, 0, -1.0);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(root -> comparator.compare(root, 1.0) == 0));
        assertTrue(result.stream().anyMatch(root -> comparator.compare(root, -1.0) == 0));
    }

    @Test
    @DisplayName("x^2 + 2*x + 1 не имеет один корень кратности два")
    void getRootOneRoot() {
        var result = squareRoot.getRoot(1.0, 2, 1.0);

        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(root -> comparator.compare(root, -1.0) == 0));
    }

    @Test
    @DisplayName("При дескрименанте больше нуля но меньше delta вернется один корень кратности два")
    void getRootDiscriminantNearZero() {
        var result = squareRoot.getRoot(1.0, 2+ZERO, 1.0);

        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(root -> comparator.compare(root, -1.0) == 0));
    }

    @Test
    @DisplayName("Если a = 0 возвращает исключение")
    void getRootNotQuadraticEquation() {
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(0.0, 2, 1.0), "Not Quadratic equation");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(ZERO, 2, 1.0), "Not Quadratic equation");
        assertDoesNotThrow(() -> squareRoot.getRoot(NOR_ZERO, 2, 1.0));
    }

    @Test
    @DisplayName("Если один из параметров NaN, возвращает исключение")
    void getRootNaN() {
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(Double.NaN, 2, 1.0), "Params is NaN");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(1, Double.NaN, 1.0), "Params is NaN");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(1, 2, Double.NaN), "Params is NaN");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(1, Double.NaN, Double.NaN), "Params is NaN");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(Double.NaN, Double.NaN, 1.0), "Params is NaN");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(Double.NaN, 2, Double.NaN), "Params is NaN");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(Double.NaN, Double.NaN, Double.NaN), "Params is NaN");
    }

    @Test
    @DisplayName("Если один из параметров INFINITY, возвращает исключение")
    void getRootInfinite() {
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(Double.NEGATIVE_INFINITY, 2, 1.0), "Params is Infinite");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(1, Double.NEGATIVE_INFINITY, 1.0), "Params is Infinite");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(1, 2, Double.NEGATIVE_INFINITY), "Params is Infinite");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(1, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), "Params is Infinite");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1.0), "Params is Infinite");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(Double.NEGATIVE_INFINITY, 2, Double.NEGATIVE_INFINITY), "Params is Infinite");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), "Params is Infinite");
    }

    @Test
    @DisplayName("Если дискриминант INFINITY, возвращает исключение")
    void getRootDiscriminantInfinite() {
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(1.0, Double.MAX_VALUE, 1.0), "Discriminant is infinite");
        assertThrows(RuntimeException.class, () -> squareRoot.getRoot(Double.MAX_VALUE, 2, Double.MAX_VALUE), "Discriminant is infinite");
    }
}
