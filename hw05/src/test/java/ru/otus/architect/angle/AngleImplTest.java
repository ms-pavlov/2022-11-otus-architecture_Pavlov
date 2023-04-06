package ru.otus.architect.angle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.architect.game.objects.dimension.angle.Angle;
import ru.otus.architect.game.objects.dimension.angle.AngleImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AngleImplTest {

    private final static double DELTA = 0.00005;
    private final static int DISCRETE_COUNT = 180;
    private final static int BAD_DISCRETE_COUNT = 0;

    @Test
    @DisplayName("дробь с нулевым или отрицательным знаменателем не допустимы для описания угла")
    void createWithBadDiscreteAngleCount() {
        assertThrows(RuntimeException.class, () -> new AngleImpl(0, BAD_DISCRETE_COUNT));
        assertThrows(RuntimeException.class, () -> new AngleImpl(0, - DISCRETE_COUNT));
    }

    @Test
    @DisplayName("можно получить угол в радианах")
    void getAngle() {
        Angle angle = new AngleImpl(DISCRETE_COUNT/2, DISCRETE_COUNT);

        assertTrue(Math.abs(angle.getAngle() - Math.PI/2) < DELTA);
    }

    @Test
    @DisplayName("можно сложить два угла")
    void add() {
        Angle angle1 = new AngleImpl(DISCRETE_COUNT/4, DISCRETE_COUNT);
        Angle angle2 = new AngleImpl(DISCRETE_COUNT/2, DISCRETE_COUNT);

        Angle result = angle1.add(angle2);

        assertTrue( Math.abs(result.getAngle() - 3*Math.PI/4) < DELTA);
    }
}