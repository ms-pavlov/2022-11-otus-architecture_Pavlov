package ru.otus.architect.adapter.plugins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.adapter.AdapterFactory;
import ru.otus.architect.game.Game;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.plugins.IoCPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdapterPluginTest {
    private final static String DEPENDENCY_NAME = "Adapter";
    public final static String SCOPE = "main";
    public final static Object TEST_OBJECT = new Object();
    public final static Class<?> TEST_CLASS = Movable.class;

    @Mock
    private BiConsumer<String, FactoryMethod> registerIoCStrategy;
    @Mock
    private Consumer<String> scopeIoCStrategy;
    @Mock
    private AdapterFactory factory;
    @Mock
    private Game game;

    private Map<String, FactoryMethod> storage;
    private IoCPlugin plugin;

    @BeforeEach
    void setUp() {
        storage = new HashMap<>();
        doAnswer(
                invocationOnMock -> storage.put(
                        invocationOnMock.getArgument(0),
                        invocationOnMock.getArgument(1)))
                .when(registerIoCStrategy)
                .accept(any(), any());
        when(game.getGameId()).thenReturn(SCOPE);
        plugin = new AdapterPlugin(registerIoCStrategy, scopeIoCStrategy, factory, game);
    }

    @Test
    @DisplayName("Регистрирует зависимость для генерации адаптера в заданом SCOPE")
    void execute() {
        plugin.execute();

        verify(scopeIoCStrategy, times(1)).accept(eq(SCOPE));
        verify(registerIoCStrategy, times(1)).accept(eq(DEPENDENCY_NAME), any());
    }

    @Test
    @DisplayName("Зависимость Adapter создает адаптер")
    void createAdapter() {

        plugin.execute();
        var result = storage.get(DEPENDENCY_NAME).create(TEST_CLASS, TEST_OBJECT);

        verify(factory, times(1)).getAdapter(eq(TEST_CLASS), eq(TEST_OBJECT));
    }
}