package ru.otus.architect.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.architect.game.objects.dimension.Coordinates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoordinatesTest {

    @Test
    @DisplayName("checks of one coordinates object plus other gives correct coordinates")
    void testPlusOnePointCoordinatesToOther() {
        var cut = new Coordinates(1.0, 1.0);

        var newPoint = cut.plus(new Coordinates(2.0, 2.0));

        assertEquals(new Coordinates(3.0, 3.0), newPoint);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    @DisplayName("checks of one coordinates object plus other gives correct coordinates")
    void testCreateIncorrectCoordinatesThrowsException(double wrongNumber) {
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(wrongNumber, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(1.0, wrongNumber));
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(wrongNumber, wrongNumber));
    }
}
