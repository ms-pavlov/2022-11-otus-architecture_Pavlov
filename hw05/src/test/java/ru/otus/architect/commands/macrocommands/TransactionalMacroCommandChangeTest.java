package ru.otus.architect.commands.macrocommands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.MoveCommandException;
import ru.otus.architect.commands.transactions.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TransactionalMacroCommandChangeTest {

    private Command macroCommand;
    private List<Command> commandQueue;

    @Mock
    private Transactional transactional;
    @Mock
    private Command command;

    @BeforeEach
    void init() {
        doNothing().when(transactional).execute();
        doNothing().when(command).execute();

        commandQueue = new LinkedList<>();
        commandQueue.add(transactional);
        commandQueue.add(command);

        macroCommand = new TransactionalMacroCommandChange(commandQueue);
    }

    @Test
    @DisplayName("Нормальное выполнение")
    void shouldOk() {
        assertDoesNotThrow(() -> macroCommand.execute());

        verify(transactional, times(1)).backup();
        verify(transactional, times(0)).rollback();
    }

    @Test
    @DisplayName("Случай, когда когда в случае исключения мы используем транзакционность ")
    void shouldUseReverseCommand() {
        commandQueue.add(() -> {
            throw new MoveCommandException(new RuntimeException("This is random exception"));
        });

        assertThrows(MoveCommandException.class, () -> macroCommand.execute());
        verify(transactional, times(1)).backup();
        verify(transactional, times(1)).rollback();
    }

    @Test
    @DisplayName("Проверяем порядок вызова транзакционных комманд")
    void shouldUseReverseCommandInOrder() {
        Transactional secondTransactional = mock(Transactional.class);
        Transactional thirdTransactional = mock(Transactional.class);
        commandQueue.add(secondTransactional);
        commandQueue.add(() -> {
            throw new MoveCommandException(new RuntimeException("This is random exception"));
        });
        commandQueue.add(thirdTransactional);
        InOrder rollbackOrder = Mockito.inOrder(secondTransactional, transactional);

        assertThrows(MoveCommandException.class, () -> macroCommand.execute());

        rollbackOrder.verify(secondTransactional).rollback();
        rollbackOrder.verify(transactional).rollback();

        verify(thirdTransactional, times(0)).execute();
        verify(thirdTransactional, times(0)).rollback();
    }
}