package ru.otus.architect.commands.macrocommands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.LinkedList;
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
class MacroCommandTest {
    private MacroCommand macroCommand;
    private Queue<Command> commandQueue;

    @Mock
    private Command firstCommand;
    @Mock
    private Command secondCommand;

    @BeforeEach
    void init() {
        commandQueue = new LinkedList<>();

        commandQueue.add(firstCommand);
        commandQueue.add(secondCommand);

        macroCommand = new MacroCommand(commandQueue);
    }

    @Test
    @DisplayName("Нормальный тест")
    void shouldOk() {
        macroCommand.execute();

        InOrder inOrder = Mockito.inOrder(firstCommand, secondCommand);
        inOrder.verify(firstCommand).execute();
        inOrder.verify(secondCommand).execute();
    }

    @Test
    @DisplayName("Тест на выборс исключения")
    void shouldThrowException() {
        Command thirdCommand = mock(Command.class);
        commandQueue.add(thirdCommand);
        doThrow(new MoveCommandException(new RuntimeException("This is random exception")))
                .when(secondCommand).execute();

        assertThrows(MoveCommandException.class, () -> macroCommand.execute());
        verify(firstCommand, times(1)).execute();
        verify(secondCommand, times(1)).execute();
        verify(thirdCommand, times(0)).execute();


    }

}