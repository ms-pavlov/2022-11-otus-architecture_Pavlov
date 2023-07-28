package ru.otus.commands.strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.otus.commands.Command;
import ru.otus.commands.CrossableCommandFactory;
import ru.otus.commands.stoppable.ChangeableCommand;
import ru.otus.mesh.PlayingField;
import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewSurroundingHandlerTest {

    @Mock
    private PlayingField field;
    @Mock
    private Crossable crossable1;
    @Mock
    private Crossable crossable2;
    @Mock
    private Command command;
    @Mock
    private Surrounding surrounding;
    @Mock
    private ChangeableCommand changeableCommand;
    @Mock
    private CrossableCommandFactory crossableCommandFactory;

    private BiConsumer<Crossable, Surrounding> consumer;

    @BeforeEach
    void setUp() {
        when(crossableCommandFactory.apply(crossable1, surrounding)).thenReturn(List.of(command));
        consumer = new NewSurroundingHandler(field, changeableCommand, crossableCommandFactory);
    }

    @Test
    @DisplayName("Перезаписывает команду проверки пересечений для новой области")
    void accept() {
        doAnswer((Answer<Void>) invocationOnMock -> {
            ((Command)invocationOnMock.getArgument(0)).execute();
            return null;
        }).
        when(changeableCommand).changeCommand(any());

        consumer.accept(crossable1, surrounding);


        verify(crossableCommandFactory, times(1)).apply(crossable1, surrounding);
        verify(changeableCommand, times(1)).changeCommand(any());
        verify(command, times(1)).execute();

    }
}