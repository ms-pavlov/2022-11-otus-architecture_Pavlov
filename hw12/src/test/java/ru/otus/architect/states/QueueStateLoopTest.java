package ru.otus.architect.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.ExceptionHandler;
import ru.otus.architect.exceptions.ExceptionHandlingStrategy;
import ru.otus.architect.loops.CommandHandler;
import ru.otus.architect.loops.QueueLoopThread;
import ru.otus.architect.loops.QueueLoopThreadImpl;
import ru.otus.architect.loops.StopHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class QueueStateLoopTest {
    @Mock
    private Command command;
    @Mock
    private Command badCommand;
    @Mock
    private BlockingQueue<Command> commands;
    @Mock
    private ExceptionHandlingStrategy handlingStrategy;
    @Mock
    private ExceptionHandler exceptionHandler;
    @Mock
    private LoopState state;

    private QueueLoop loopThread;

    @BeforeEach
    void setUp() {
        this.loopThread = new QueueStateLoop(commands, handlingStrategy, state);
    }


    @Test
    @DisplayName("Исключение при выполнении команд не прекращает обработку команд")
    void commandException() throws InterruptedException {
        BlockingQueue<Command> queue = new ArrayBlockingQueue<>(10);
        queue.add(this.command);
        queue.add(this.badCommand);
        queue.add(this.command);
        when(this.commands.take()).thenAnswer((invocationOnMock) -> queue.take());
        when(this.handlingStrategy.getHandler(any(), any())).thenReturn(this.exceptionHandler);
        doNothing().when(state).accept(command);
        doThrow(RuntimeException.class).when(state).accept(badCommand);
        when(state.next()).thenAnswer(invocationOnMock -> {
            if (queue.isEmpty()) {
                return null;
            }
            return state;
        });

        this.loopThread.run();

        verify(this.state, times(2)).accept(command);
        verify(this.state, times(1)).accept(badCommand);
        verify(this.handlingStrategy, times(1)).getHandler(any(), any());
    }

    @Test
    @DisplayName("Исключение при обращении к очереди команд не прекращает обработку команд")
    void queueException() throws InterruptedException {
        BlockingQueue<Command> queue = new ArrayBlockingQueue<>(10);
        queue.add(this.command);
        queue.add(this.command);
        queue.add(this.command);
        when(this.commands.take()).thenAnswer((invocationOnMock) -> {
            Command com = queue.take();
            if (queue.size() == 1) {
                throw new RuntimeException();
            } else {
                return com;
            }
        });
        when(this.handlingStrategy.getHandler(any(), any())).thenReturn(this.exceptionHandler);
        doNothing().when(state).accept(command);
        when(state.next()).thenAnswer(invocationOnMock -> {
            if (queue.isEmpty()) {
                return null;
            }
            return state;
        });


        this.loopThread.run();

        verify(state, times(2)).accept(command);
        verify(this.handlingStrategy, times(1)).getHandler(any(), any());
    }

    @Test
    @DisplayName("Возвращает текущее состояние")
    void getState() {
        assertEquals(state, loopThread.getState());
    }
}