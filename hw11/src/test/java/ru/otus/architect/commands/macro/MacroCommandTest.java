package ru.otus.architect.commands.macro;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.CommandException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class MacroCommandTest {

    @Mock
    Command command1;

    @Mock
    Command command2;

    @Mock
    Command throwingExceptionCommand;

    @Test
    @DisplayName("Макрокоманда выполняет все переданные команды")
    void testExecute() {
        // arrange
        var cut = new MacroCommand(List.of(command1, command2));
        // act
        cut.execute();
        // assert
        verify(command1, times(1)).execute();
        verify(command2, times(1)).execute();
    }


    @Test
    @DisplayName("Исключение должно быть обработано и макрокоманда должна выбросить CommandException")
    void testExceptionLoggingHandler() {
        // arrange
        doThrow(new RuntimeException()).when(throwingExceptionCommand).execute();
        var cut = new MacroCommand(List.of(command1, throwingExceptionCommand));
        //act
        assertThrows(CommandException.class, cut::execute);
        // assert
        verify(command1, Mockito.times(1)).execute();
        verify(throwingExceptionCommand, Mockito.times(1)).execute();
    }
}