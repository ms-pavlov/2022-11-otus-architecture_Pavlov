package ru.otus.architect.game.objects.dimension.vector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VectorDecoratorTest {

    private final static Integer X_COORDINATE = 1;
    private final static Integer Y_COORDINATE = 2;
    private final static Vector TEST_2D_VECTOR = new VectorImpl(X_COORDINATE, Y_COORDINATE);
    private final static Vector TEST_1D_VECTOR = new VectorImpl(X_COORDINATE);


    @Test
    @DisplayName("Для вектора с размерностью 2 или более, возвращает координаты x и y ")
    void getCoordinatesAsString() {
        var result = new VectorDecorator(TEST_2D_VECTOR)
                .getCoordinatesAsString();

        assertTrue(result.containsKey("coordinate.x"));
        assertEquals(X_COORDINATE.toString(), result.get("coordinate.x"));
        assertTrue(result.containsKey("coordinate.y"));
        assertEquals(Y_COORDINATE.toString(), result.get("coordinate.y"));
    }

    @Test
    @DisplayName("Для вектора с размерностью меньше 2, возвращает координаты списком")
    void get1DCoordinatesAsString() {
        var result = new VectorDecorator(TEST_1D_VECTOR)
                .getCoordinatesAsString();

        assertTrue(result.containsKey("coordinates"));
        assertEquals(TEST_1D_VECTOR.getCoordinates().toString(), result.get("coordinates"));
    }
}