package ru.otus.architect.ioc.plugins.gen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.Game;
import ru.otus.architect.game.GameObject;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.plugins.IoCPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameObjectMovableAdapterPluginTest {
    public final static String SCOPE = "test";
    public final static Object TEST_OBJECT = new Object();
    @Mock
    private BiConsumer<String, FactoryMethod> registerIoCStrategy;
    @Mock
    private Consumer<String> scopeIoCStrategy;
    @Mock
    private Game game;
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
        plugin = new GameObjectMovableAdapterPlugin(registerIoCStrategy, scopeIoCStrategy, game);
    }

    @Test
    @DisplayName("Плагин должен добавлять все зависимости")
    void execute() {
        plugin.execute();

        verify(scopeIoCStrategy, times(1)).accept(eq(SCOPE));
        verify(registerIoCStrategy, times(1)).accept(eq("IoC.GameObjectImpl.Movable::getPosition"), any());
        verify(registerIoCStrategy, times(1)).accept(eq("IoC.GameObjectImpl.Movable::setPosition"), any());
        verify(registerIoCStrategy, times(1)).accept(eq("IoC.GameObjectImpl.Movable::getVelocity"), any());
    }

    @Test
    @DisplayName("Зависимость IoC.GameObjectImpl.Movable::getPosition получает позицию игрового объекта")
    void getPosition() {
        when(gameObject.getParameter("position")).thenReturn(TEST_OBJECT);

        plugin.execute();
        var result = storage.get("IoC.GameObjectImpl.Movable::getPosition").create(gameObject);

        verify(gameObject, times(1)).getParameter(eq("position"));
        assertEquals(TEST_OBJECT, result);
    }

    @Test
    @DisplayName("Зависимость IoC.GameObjectImpl.Movable::setPosition уставнавливает позицию игрового объекта")
    void setPosition() {
        plugin.execute();
        storage.get("IoC.GameObjectImpl.Movable::setPosition").create(gameObject, TEST_OBJECT);

        verify(gameObject, times(1)).setParameter("position", TEST_OBJECT);
    }

    @Test
    @DisplayName("Зависимость IoC.GameObjectImpl.Movable::getVelocity получает ускорение игрового объекта")
    void getVelocity() {
        when(gameObject.getParameter("velocity")).thenReturn(TEST_OBJECT);

        plugin.execute();
        var result = storage.get("IoC.GameObjectImpl.Movable::getVelocity").create(gameObject);

        verify(gameObject, times(1)).getParameter(eq("velocity"));
        assertEquals(TEST_OBJECT, result);
    }
}