package ru.otus.architect.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.openapi.model.Message;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterpretCommandTest {
    public final static UUID GAME_UUID = UUID.randomUUID();
    @Mock
    private BiConsumer<String, Command> commandConsumerStrategy;
    @Mock
    private Function<Message, Command> commandBuilderStrategy;
    @Mock
    private Message message;
    @Mock
    private Command command;

    private Command interpretCommand;

    @BeforeEach
    void setUp() {
        interpretCommand = new InterpretCommand(
                commandConsumerStrategy,
                commandBuilderStrategy,
                message);
    }

    @Test
    @DisplayName("InterpretCommand создает команду и помещает её в очередь")
    void execute() {
        when(commandBuilderStrategy.apply(message)).thenReturn(command);
        when(message.getGame()).thenReturn(GAME_UUID);

        interpretCommand.execute();

        verify(commandBuilderStrategy, times(1)).apply(message);
        verify(commandConsumerStrategy, times(1)).accept(GAME_UUID.toString(), command);
    }
}