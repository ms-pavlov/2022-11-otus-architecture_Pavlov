package ru.otus.architect.commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.objects.dimension.angle.Angle;
import ru.otus.architect.game.objects.dimension.angle.AngleImpl;
import ru.otus.architect.game.objects.characteristic.Rotation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RotateCommandTest {
    private final static Angle TEST_ANGLE_1 = new AngleImpl(4, 18);
    private final static Angle TEST_ANGLE_2 = new AngleImpl(50, 180);
    private final static Angle TEST_SUM_RESULT = new AngleImpl(9, 18);

    @Mock
    private Rotation rotation;

    @Test
    @DisplayName("можно выполнить")
    void execute() {
        when(rotation.getAngle())
                .thenReturn(TEST_ANGLE_1);
        when(rotation.getAngularVelocity())
                .thenReturn(TEST_ANGLE_2);

        Command command = new RotateCommand(rotation);
        command.execute();

        verify(rotation, times(1)).setAngle(TEST_SUM_RESULT);
    }

    @Test
    @DisplayName("нельзя повернуть не зная текущий угол")
    void badAngle () {
        doThrow(new RuntimeException())
                .when(rotation).getAngle();

        Command command = new RotateCommand(rotation);

        assertThrows(RotateCommandException.class, command::execute);
    }

    @Test
    @DisplayName("нельзя повернуть не зная угловое ускорение")
    void badAngularVelocity() {
        when(rotation.getAngle())
                .thenReturn(TEST_ANGLE_1);
        doThrow(new RuntimeException())
                .when(rotation).getAngularVelocity();

        Command command = new RotateCommand(rotation);

        assertThrows(RotateCommandException.class, command::execute);
    }

    @Test
    @DisplayName("нельзя повернуть не вращаемое")
    void rotateNotRotatable() {
        when(rotation.getAngle())
                .thenReturn(TEST_ANGLE_1);
        when(rotation.getAngularVelocity())
                .thenReturn(TEST_ANGLE_2);
        doThrow(new RuntimeException())
                .when(rotation).setAngle(any());

        Command command = new RotateCommand(rotation);

        assertThrows(RotateCommandException.class, command::execute);
    }
}