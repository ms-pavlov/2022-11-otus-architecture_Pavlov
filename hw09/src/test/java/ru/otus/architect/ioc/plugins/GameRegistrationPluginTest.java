package ru.otus.architect.ioc.plugins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.game.Game;
import ru.otus.architect.game.GameObject;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.openapi.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameRegistrationPluginTest {

    public final static String SCOPE = "test";
    public final static String ACTION_NAME = "action";
    public final static Message MESSAGE = new Message()
            .gameObject(1L)
            .action(2L);
    @Mock
    private BiConsumer<String, FactoryMethod> registerIoCStrategy;
    @Mock
    private Consumer<String> scopeIoCStrategy;
    @Mock
    private Game game;
    @Mock
    private Command command;
    @Mock
    private GameObject gameObject;

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
        plugin = new GameRegistrationPlugin(registerIoCStrategy, scopeIoCStrategy, game);
    }

    @Test
    @DisplayName("Плагин должен добавлять все зависимости")
    void execute() {
        plugin.execute();

        verify(scopeIoCStrategy, times(1)).accept(eq(SCOPE));
        verify(registerIoCStrategy, times(1)).accept(eq("IoC.Game"), any());
        verify(registerIoCStrategy, times(1)).accept(eq("IoC.Game::addCommand"), any());
        verify(registerIoCStrategy, times(1)).accept(eq("IoC.Game::getGameObject"), any());
        verify(registerIoCStrategy, times(1)).accept(eq("IoC.Game::getActionName"), any());
        verify(registerIoCStrategy, times(1)).accept(eq("IoC.Game::pollCommand"), any());

    }

    @Test
    @DisplayName("Зависимость IoC.Game должна возврашать игру")
    void getGame() {
        plugin.execute();
        var result = storage.get("IoC.Game");

        assertEquals(game, result.create());
    }

    @Test
    @DisplayName("Зависимость IoC.Game::addCommand должна добавлять команду в очередь")
    void addCommand() {
        plugin.execute();
        var result = storage.get("IoC.Game::addCommand");
        result.create(command);

        verify(game, times(1)).addCommand(command);
    }

    @Test
    @DisplayName("Зависимость IoC.Game::getGameObject должна возвращать игровой обект")
    void getGameObject() {
        when(game.getGameObject(MESSAGE.getGameObject())).thenReturn(gameObject);

        plugin.execute();
        var result = storage.get("IoC.Game::getGameObject").create(MESSAGE);

        assertEquals(gameObject, result);
        verify(game, times(1)).getGameObject(MESSAGE.getGameObject());
    }

    @Test
    @DisplayName("Зависимость IoC.Game::getActionName должна возвращать навзвание действия над игровым обектом")
    void getActionName() {
        when(game.getActionName(MESSAGE.getGameObject(), MESSAGE.getAction())).thenReturn(ACTION_NAME);

        plugin.execute();
        var result = storage.get("IoC.Game::getActionName").create(MESSAGE);

        assertEquals(ACTION_NAME, result);
        verify(game, times(1)).getActionName(MESSAGE.getGameObject(), MESSAGE.getAction());
    }

    @Test
    @DisplayName("Зависимость IoC.Game::pollCommand должна извлекает команду из очереди")
    void pollCommand() {
        when(game.pollCommand()).thenReturn(command);

        plugin.execute();
        var result = storage.get("IoC.Game::pollCommand").create();

        assertEquals(command, result);
        verify(game, times(1)).pollCommand();
    }
}