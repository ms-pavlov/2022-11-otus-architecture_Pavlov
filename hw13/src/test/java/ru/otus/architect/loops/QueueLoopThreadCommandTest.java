package ru.otus.architect.loops;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;

import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class QueueLoopThreadCommandTest {
    @Mock
    private QueueLoopThread loopThread;
    @Mock
    private ExecutorService executorService;
    private Command command;

    QueueLoopThreadCommandTest() {
    }

    @BeforeEach
    void setUp() {
        this.command = new QueueLoopThreadCommand(this.loopThread, this.executorService);
    }

    @Test
    @DisplayName("Команда запускает выполнение цикла обработки")
    void execute() {
        this.command.execute();
        verify(this.executorService, times(1))
                .execute(this.loopThread);
    }
}