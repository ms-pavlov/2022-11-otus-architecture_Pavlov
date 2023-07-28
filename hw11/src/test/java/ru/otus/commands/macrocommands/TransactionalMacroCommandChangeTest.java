package ru.otus.commands.macrocommands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.commands.Command;
import ru.otus.commands.transactions.Transactional;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
            throw new RuntimeException("This is random exception");
        });

        assertThrows(RuntimeException.class, () -> macroCommand.execute(), "This is random exception");
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
            throw new RuntimeException("This is random exception");
        });
        commandQueue.add(thirdTransactional);
        InOrder rollbackOrder = Mockito.inOrder(secondTransactional, transactional);

        assertThrows(RuntimeException.class, () -> macroCommand.execute(), "This is random exception");

        rollbackOrder.verify(secondTransactional).rollback();
        rollbackOrder.verify(transactional).rollback();

        verify(thirdTransactional, times(0)).execute();
        verify(thirdTransactional, times(0)).rollback();
    }
}