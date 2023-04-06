package ru.otus.architect.commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.objects.dimension.angle.Angle;
import ru.otus.architect.game.objects.dimension.angle.AngleImpl;
import ru.otus.architect.game.objects.characteristic.AngularAccelerator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AngularAccelerationCommandTest {

    private final static Angle TEST_ANGLE_1 = new AngleImpl(4, 18);
    private final static Angle TEST_ANGLE_2 = new AngleImpl(50, 180);
    private final static Angle TEST_SUM_RESULT = new AngleImpl(9, 18);


    @Mock
    private AngularAccelerator angularAccelerator;

    @Test
    @DisplayName("можно выполнить")
    void execute() {
        when(angularAccelerator.getAngularVelocity())
                .thenReturn(TEST_ANGLE_1);
        when(angularAccelerator.getAngularAcceleration())
                .thenReturn(TEST_ANGLE_2);

        Command command = new AngularAccelerationCommand(angularAccelerator);
        command.execute();

        verify(angularAccelerator, times(1)).setAngularVelocity(TEST_SUM_RESULT);
    }

    @Test
    @DisplayName("нельзя ускорить не зная текущей угловой скорости")
    void badAngularVelocity() {
        doThrow(new RuntimeException())
                .when(angularAccelerator).getAngularVelocity();

        Command command = new AngularAccelerationCommand(angularAccelerator);

        assertThrows(AngularAccelerationCommandException.class, command::execute);
    }

    @Test
    @DisplayName("нельзя ускорить не зная ускорения")
    void badAngularAcceleration() {
        when(angularAccelerator.getAngularVelocity())
                .thenReturn(TEST_ANGLE_1);
        doThrow(new RuntimeException())
                .when(angularAccelerator).getAngularAcceleration();

        Command command = new AngularAccelerationCommand(angularAccelerator);

        assertThrows(AngularAccelerationCommandException.class, command::execute);
    }

    @Test
    @DisplayName("нельзя ускорить не ускоряемое")
    void accelerateNoAngularAccelerator() {
        when(angularAccelerator.getAngularVelocity())
                .thenReturn(TEST_ANGLE_1);
        when(angularAccelerator.getAngularAcceleration())
                .thenReturn(TEST_ANGLE_2);
        doThrow(new RuntimeException())
                .when(angularAccelerator).setAngularVelocity(any());

        Command command = new AngularAccelerationCommand(angularAccelerator);

        assertThrows(AngularAccelerationCommandException.class, command::execute);
    }
}