package ru.otus.architect.loops;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class QueueLoopThreadImplTest {
    @Mock
    private Command command;
    @Mock
    private Command badCommand;
    @Mock
    private Supplier<Command> commandSupplierStrategy;
    @Mock
    private ExceptionHandlingStrategy handlingStrategy;
    @Mock
    private ExceptionHandler exceptionHandler;
    @Mock
    private CommandHandler commandHandler;
    @Mock
    private StopHandler stopHandler;
    private QueueLoopThread loopThread;

    QueueLoopThreadImplTest() {
    }

    @BeforeEach
    void setUp() {
        this.loopThread = new QueueLoopThreadImpl(this.commandSupplierStrategy, this.handlingStrategy, this.commandHandler, this.stopHandler);
    }

    @Test
    @DisplayName("Можно поменять стратегию обработки команд")
    void setCommandHandler() {
        List<Command> commandsList = new ArrayList<>();
        when(this.stopHandler.get()).thenAnswer((invocationOnMock) -> commandsList.isEmpty());
        when(this.commandSupplierStrategy.get()).thenReturn(this.command);
        loopThread.setCommandHandler(commandsList::add);

        this.loopThread.run();

        verify(this.stopHandler, Mockito.times(2)).get();
        verify(this.commandHandler, Mockito.times(0)).accept(any());
    }

    @Test
    @DisplayName("Можно поменять стратегию остоновки")
    void setStopHandler() {
        this.loopThread.setStopHandler(() -> false);

        this.loopThread.run();

        verify(this.stopHandler, Mockito.times(0)).get();
    }

    @Test
    @DisplayName("Исключение при выполнении команд не прекращает обработку команд")
    void commandException() {
        BlockingQueue<Command> queue = new ArrayBlockingQueue<>(10);
        queue.add(this.command);
        queue.add(this.badCommand);
        queue.add(this.command);
        doThrow(RuntimeException.class).when(this.badCommand).execute();
        doNothing().when(this.command).execute();
        when(this.commandSupplierStrategy.get()).thenAnswer((invocationOnMock) -> queue.take());
        when(this.stopHandler.get()).thenAnswer((invocationOnMock) -> !queue.isEmpty());
        when(this.handlingStrategy.getHandler(any(), any())).thenReturn(this.exceptionHandler);
        this.loopThread.setCommandHandler(Command::execute);

        this.loopThread.run();

        verify(this.command, Mockito.times(2)).execute();
        verify(this.badCommand, Mockito.times(1)).execute();
        verify(this.handlingStrategy, Mockito.times(1)).getHandler(any(), any());
    }

    @Test
    @DisplayName("Исключение при обращении к очереди команд не прекращает обработку команд")
    void queueException() {
        BlockingQueue<Command> queue = new ArrayBlockingQueue<>(10);
        queue.add(this.command);
        queue.add(this.command);
        queue.add(this.command);
        doNothing().when(this.command).execute();
        when(this.commandSupplierStrategy.get()).thenAnswer((invocationOnMock) -> {
            Command com = queue.take();
            if (queue.size() == 1) {
                throw new RuntimeException();
            } else {
                return com;
            }
        });
        when(this.stopHandler.get()).thenAnswer((invocationOnMock) -> !queue.isEmpty());
        when(this.handlingStrategy.getHandler(any(), any())).thenReturn(this.exceptionHandler);
        this.loopThread.setCommandHandler(Command::execute);

        this.loopThread.run();

        verify(this.command, Mockito.times(2)).execute();
        verify(this.handlingStrategy, Mockito.times(1)).getHandler(any(), any());
    }
}
