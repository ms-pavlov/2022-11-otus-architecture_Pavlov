package ru.otus.architect.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GamesStorageImplTest {

    private final static UUID UUID = java.util.UUID.randomUUID();

    @Mock
    private Map<UUID, Game> games;
    @Mock
    private Game game;
    @Mock
    private Supplier<UUID> idGenerator;
    @Mock
    private GameObjectStorage objectStorage;

    private GamesStorage storage;

    @BeforeEach
    void setUp() {
        storage = new GamesStorageImpl(games, idGenerator);
    }

    @Test
    void getGame() {
        when(games.get(UUID)).thenReturn(game);

        var result = storage.getGame(UUID);

        assertEquals(game, result);
        verify(games, times(1)).get(UUID);

    }

    @Test
    void createGame() {
        when(idGenerator.get()).thenReturn(UUID);

        var result = storage.createGame(objectStorage);

        verify(games, times(1)).put(UUID, result);
    }

    @Test
    void defaultConstructor() {
        assertDoesNotThrow(() -> new GamesStorageImpl());
    }
}