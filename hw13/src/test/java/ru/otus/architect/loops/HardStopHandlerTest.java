package ru.otus.architect.loops;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class HardStopHandlerTest {

    @Test
    @DisplayName("Возвращает false")
    void get() {
        var stopHandler = new HardStopHandler();

        assertFalse(stopHandler.get());
    }
}