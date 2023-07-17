package ru.otus.architect.ioc.plugins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.api.AnswerConsumer;
import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.CommandFactory;
import ru.otus.architect.game.Game;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.openapi.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CommandsPluginTest {

    public final static String SCOPE = "test";

    @Mock
    private BiConsumer<String, FactoryMethod> registerIoCStrategy;
    @Mock
    private Consumer<String> scopeIoCStrategy;
    @Mock
    private CommandFactory commandFactory;
    @Mock
    private Game game;
    @Mock
    private Message message;
    @Mock
    private AnswerConsumer answerConsumer;
    @Mock
    private Command command;

    private IoCPlugin plugin;
    private Map<String, FactoryMethod> storage;


    @BeforeEach
    void setUp() {
        storage = new HashMap<>();
        when(game.getGameId()).thenReturn(SCOPE);
        doAnswer(
                invocationOnMock -> storage.put(
                        invocationOnMock.getArgument(0),
                        invocationOnMock.getArgument(1)))
                .when(registerIoCStrategy)
                .accept(any(), any());
        plugin = new CommandsPlugin(registerIoCStrategy, scopeIoCStrategy, commandFactory, game);
    }

    @Test
    @DisplayName("Плагин должен добавлять все зависимости")
    void execute() {
        plugin.execute();

        verify(scopeIoCStrategy, times(1)).accept(eq(SCOPE));
        verify(registerIoCStrategy, times(1)).accept(eq("Game.MoveCommand"), any());
        verify(registerIoCStrategy, times(1)).accept(eq("Message.InterpretCommand"), any());

    }

    @Test
    @DisplayName("Зависимость Game.MoveCommand должна создавать команду")
    void getMoveCommand() {
        when(commandFactory.create("Game.MoveCommand", message, answerConsumer)).thenReturn(command);

        plugin.execute();
        var result = storage.get("Game.MoveCommand");

        assertEquals(command, result.create(message, answerConsumer));
        verify(commandFactory, times(1)).create("Game.MoveCommand", message, answerConsumer);
    }

    @Test
    @DisplayName("Зависимость Message.InterpretCommand должна создавать команду")
    void getInterpretCommand() {
        when(commandFactory.create("Message.InterpretCommand", message, answerConsumer)).thenReturn(command);

        plugin.execute();
        var result = storage.get("Message.InterpretCommand");

        assertEquals(command, result.create(message, answerConsumer));
        verify(commandFactory, times(1)).create("Message.InterpretCommand", message, answerConsumer);
    }
}