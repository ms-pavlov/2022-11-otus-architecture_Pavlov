package ru.otus.architect.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.objects.characteristic.FuelConsumer;

@ExtendWith(MockitoExtension.class)
class FuelBurnerTest {

    @Mock
    private FuelConsumer burnable;
    public BurnFuelCommand fuelBurner;
    private final int startFuel = 10;

    private final int consumption = 3;

    @BeforeEach
    void init() {
        when(burnable.getFuelLevel()).thenReturn(startFuel);
        when(burnable.getConsumption()).thenReturn(consumption);
        fuelBurner = new BurnFuelCommand(burnable);
    }

    @Test
    @DisplayName("Положительный тест")
    void normalTest() {
        assertDoesNotThrow(() -> fuelBurner.execute());
        //проверка, что значение топлива менялось один раз
        verify(burnable, times(1)).setFuelLvl(any());
        //проверка, что изменения были на нужное нам значение
        verify(burnable, times(1)).setFuelLvl(startFuel - consumption);
    }

    @Test
    @DisplayName("Случай, когда расходуем топливо \"в ноль\"")
    void zeroFuelTest() {
        int consumption = burnable.getConsumption();
        when(burnable.getFuelLevel()).thenReturn(consumption);

        assertDoesNotThrow(() -> fuelBurner.execute());
    }

    @Test
    @DisplayName("Ошибка, когда пытаемся израсходовать больше топлива, чем есть")
    void negativedFuelTest() {
        when(burnable.getConsumption()).thenReturn(1000);
        assertThrows(Exception.class, () -> fuelBurner.execute());
        verify(burnable, times(0)).setFuelLvl(any());
    }
}