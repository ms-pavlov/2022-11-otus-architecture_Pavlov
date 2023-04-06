package ru.otus.architect.loops;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;

import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class SoftStopCommandTest {

    @Mock
    private QueueLoopThread loopThread;
    @Mock
    private BlockingQueue<Command> commands;

    private Command command;

    @BeforeEach
    void setUp() {
        command = new SoftStopCommand(loopThread, commands);
    }

    @Test
    @DisplayName("Заменяет обработчик остановки у QueueLoopThread")
    void execute() {
        command.execute();

        verify(loopThread, times(1)).setStopHandler(any());

    }
}