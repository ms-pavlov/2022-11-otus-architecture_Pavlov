package ru.otus.architect.commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueueOfferCommandTest {

    @Mock
    private Queue<Command> queue;
    @Mock
    private Command testCommand;

    private Command  queueOfferCommand;

    @Test
    @DisplayName("Проверяем, что если в очередь можно вставить команду, не возникает исключений")
    void execute() {
        when(queue.offer(testCommand)).thenReturn(true);

        queueOfferCommand = new QueueOfferCommand(queue,testCommand);

        assertDoesNotThrow(() -> queueOfferCommand.execute());

        verify(queue, times(1)).offer(testCommand);
    }

    @Test
    @DisplayName("Проверяем, что если в очередь нельзя вставить команду, возникает исключение")
    void whenCanNotOfferExecute() {
        when(queue.offer(testCommand)).thenReturn(false);

        queueOfferCommand = new QueueOfferCommand(queue,testCommand);

        assertThrows(RuntimeException.class, () -> queueOfferCommand.execute());
    }

    @Test
    @DisplayName("Проверяем, что если в очередь null, возникает исключение")
    void whenQueueIsNullExecute() {
        queueOfferCommand = new QueueOfferCommand(null,testCommand);

        assertThrows(RuntimeException.class, () -> queueOfferCommand.execute());
    }
}