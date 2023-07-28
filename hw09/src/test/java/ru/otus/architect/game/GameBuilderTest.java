package ru.otus.architect.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.ioc.plugins.IoCPlugin;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameBuilderTest {

    private final static UUID GAME_UUID = UUID.randomUUID();
    private final static Long TEST_ID = 1L;
    private final static String ACTION_NAME = "test";

    @Mock
    private GameObjectStorage objectStorage;
    @Mock
    private GameObject gameObject;
    @Mock
    private IoCPlugin plugin;

    private GameBuilder gameBuilder;

    @BeforeEach
    void setUp() {
        gameBuilder = GameBuilder.builder(objectStorage);
    }

    @Test
    @DisplayName("Билдер устанавливает id")
    void id() {
        var result = gameBuilder.id(GAME_UUID).build();

        assertEquals(GAME_UUID.toString(), result.getGameId());
    }

    @Test
    @DisplayName("Билдер добавляет плагины для дальнейшего исполнения")
    void addPlugin() {
        gameBuilder.addPlugin(game -> plugin).build();

        verify(plugin, times(1)).execute();
    }

    @Test
    @DisplayName("Билдер добавляет плагины для дальнейшего исполнения")
    void addPlugins() {
        gameBuilder.addPlugins(List.of(game -> plugin)).build();

        verify(plugin, times(1)).execute();
    }

    @Test
    @DisplayName("Билдер добавляет игровые обекты")
    void addGameObject() {
        var result = gameBuilder.addGameObject(TEST_ID, gameObject).build();
        result.getGameObject(TEST_ID);

        verify(objectStorage, times(1)).putGameObject(TEST_ID, gameObject);
        verify(objectStorage, times(1)).getGameObject(TEST_ID);
    }

    @Test
    void addActionName() {
        var result = gameBuilder.addActionName(TEST_ID, ACTION_NAME).build();

        verify(objectStorage, times(1)).addActionName(TEST_ID, ACTION_NAME);
    }
}