package ru.otus.architect.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameObjectStorageFactoryImplTest {

    private final static GameObjectStorage GAME_OBJECT_STORAGE = new GameObjectStorageImpl();

    @Mock
    private Supplier<GameObjectStorage> gameObjectStorageInitStrategy;

    private GameObjectStorageFactory factory;

    @BeforeEach
    void setUp() {
        factory = new GameObjectStorageFactoryImpl(gameObjectStorageInitStrategy);
    }


    @Test
    @DisplayName("Создает хранилище обектов")
    void create() {
        when(gameObjectStorageInitStrategy.get()).thenReturn(GAME_OBJECT_STORAGE);

        var result = factory.create();

        assertEquals(GAME_OBJECT_STORAGE, result);
        verify(gameObjectStorageInitStrategy, times(1)).get();

    }
}