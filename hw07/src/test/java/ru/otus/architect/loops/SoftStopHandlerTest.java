package ru.otus.architect.loops;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;

import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class SoftStopHandlerTest {
    @Mock
    private BlockingQueue<Command> commands;
    private StopHandler handler;

    SoftStopHandlerTest() {
    }

    @BeforeEach
    void setUp() {
        this.handler = new SoftStopHandler(this.commands);
    }

    @Test
    @DisplayName("Возвращает true пока очеред не пуста")
    void getTrue() {
        when(this.commands.isEmpty()).thenReturn(false);
        assertTrue(this.handler.get());
    }

    @Test
    @DisplayName("Возвращает false пока очеред пуста")
    void getFalse() {
        when(this.commands.isEmpty()).thenReturn(true);
        assertFalse(this.handler.get());
    }
}