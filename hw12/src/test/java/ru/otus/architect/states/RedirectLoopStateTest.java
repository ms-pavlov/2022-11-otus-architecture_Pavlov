package ru.otus.architect.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class RedirectLoopStateTest {

    @Mock
    private Command command;
    @Mock
    private BlockingQueue<Command> queue;

    private LoopState state;

    @BeforeEach
    void setUp() {
        state = new RedirectLoopState(queue);
    }

    @Test
    @DisplayName("После выполнения doDefault новое состояние выполняет команды")
    void doDefault() {
        state.doDefault();

        LoopState next = state.next();

        assertNotEquals(state, next);

        next.accept(command);

        verify(command, times(1)).execute();
    }

    @Test
    @DisplayName("После выполнения stop следующее состояние равно null")
    void stop() {
        state.stop();

        assertNull(state.next());
    }

    @Test
    @DisplayName("next() возвращает текущее состояние")
    void next() {
        assertEquals(state, state.next());

    }

    @Test
    @DisplayName("Пренаправляет команды в другую очередь")
    void accept() throws InterruptedException {
        state.accept(command);

        verify(queue, times(1)).put(command);
    }

    @Test
    @DisplayName("Возвращает исключение если добавить команду в новую очерердь невозможно")
    void acceptException() throws InterruptedException {
        doThrow(InterruptedException.class).when(queue).put(command);

        assertThrows(RuntimeException.class, () -> state.accept(command));
    }
}