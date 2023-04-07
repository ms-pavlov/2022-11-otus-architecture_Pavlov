package ru.otus.architect.commands.macrocommands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

@ExtendWith(MockitoExtension.class)
class TransactionalMacroCommandTest {

    private TransactionalMacroCommand macroCommand;
    private Queue<Command> commandQueue;
    private List<Command> reverseList;

    @Mock
    private Command command;
    @Mock
    private Command reveseCommand;

    @BeforeEach
    void init() {
        commandQueue = new LinkedList<>();
        reverseList = new ArrayList<>();

        commandQueue.add(command);

        macroCommand = new TransactionalMacroCommand(commandQueue, reverseList);
    }

    @Test
    @DisplayName("Нормальное выполнение")
    void shouldOk() {
        reverseList.add(reveseCommand);

        assertDoesNotThrow(() -> macroCommand.execute());
        verifyNoInteractions(reveseCommand);
    }

    @Test
    @DisplayName("Случай, когда некорректно собран список обратных действий")
    void shouldThrowIncorrectReverseListException() {
        assertThrows(IncorrectReverseCommandListException.class, () -> macroCommand.execute());
    }

    @Test
    @DisplayName("Случай, когда когда в случае исключения мы используем транзакционность ")
    void shouldUseReverseCommand() {
        commandQueue.add(() -> {
            throw new MoveCommandException(new RuntimeException("This is random exception"));
        });
        reverseList.add(reveseCommand);
        reverseList.add(reveseCommand);

        assertThrows(MoveCommandException.class, () -> macroCommand.execute());
        verify(reveseCommand, times(1)).execute();
    }

    @Test
    @DisplayName("Проверяем порядок вызова транзакционных комманд")
    void shouldUseReverseCommandInOrder() {
        Command secondReverseCommand = mock(Command.class);
        commandQueue.add(command);
        commandQueue.add(() -> {
            throw new MoveCommandException(new RuntimeException("This is random exception"));
        });
        reverseList.add(reveseCommand);
        reverseList.add(secondReverseCommand);
        reverseList.add(() -> System.out.println("You will never see that text"));
        InOrder inOrder = Mockito.inOrder(reveseCommand, secondReverseCommand);

        assertThrows(MoveCommandException.class, () -> macroCommand.execute());
        inOrder.verify(secondReverseCommand).execute();
        inOrder.verify(reveseCommand).execute();
    }

}