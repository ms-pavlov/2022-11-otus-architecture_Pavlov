package ru.otus.architect.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;

import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class DefaultLoopStateTest {

    @Mock
    private Command command;
    @Mock
    private BlockingQueue<Command> queue;

    private LoopState state;

    @BeforeEach
    void setUp() {
        state = new DefaultLoopState();
    }

    @Test
    @DisplayName("После выполнения moveTo новое состояние пренаправляет команды в другую очередь")
    void moveTo() throws InterruptedException {
        state.moveTo(queue);

        LoopState next = state.next();

        assertNotEquals(state, next);

        next.accept(command);

        verify(queue, times(1)).put(command);
    }

    @Test
    @DisplayName("После выполнения stop следующее состояние равно null")
    void stop() {
        state.stop();

        assertNull(state.next());
    }

    @Test
    @DisplayName("Выполняет команды")
    void accept() {
        state.accept(command);

        verify(command, times(1)).execute();
    }

    @Test
    @DisplayName("next() возвращает текущее состояние")
    void next() {
        assertEquals(state, state.next());

    }
}