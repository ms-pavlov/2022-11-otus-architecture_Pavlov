package ru.otus.architect.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameObjectStorageImplTest {
    private final static Long GAME_OBJECT_ID = 1L;
    private final static Map<Long, GameObject> GAME_OBJECTS = Map.of(GAME_OBJECT_ID, new GameObjectImpl());
    private final static Long ACTION_NAME_ID = 0L;
    private final static Map<Long, String> ACTION_NAMES = Map.of(ACTION_NAME_ID, "test");

    private GameObjectStorage storage;

    @BeforeEach
    void setUp() {
        storage = new GameObjectStorageImpl();
    }

    @Test
    void getAllGameObjects() {
        storage.putGameObject(GAME_OBJECT_ID, GAME_OBJECTS.get(GAME_OBJECT_ID));

        var result = storage.getAllGameObjects();

        assertEquals(GAME_OBJECTS.size(), result.size());
        GAME_OBJECTS.keySet()
                .forEach(key -> assertEquals(GAME_OBJECTS.get(key), result.get(key)));
    }

    @Test
    void getGameObjectsActionNames() {
        storage.putGameObject(GAME_OBJECT_ID, GAME_OBJECTS.get(GAME_OBJECT_ID));
        storage.addActionName(GAME_OBJECT_ID, ACTION_NAMES.get(ACTION_NAME_ID));

        var result = storage.getGameObjectsActionNames(GAME_OBJECT_ID);

        assertEquals(ACTION_NAMES.size(), result.size());

        ACTION_NAMES.keySet()
                .forEach(key -> assertEquals(ACTION_NAMES.get(key), result.get(key)));
    }

    @Test
    void putAndGetGameObject() {
        storage.putGameObject(GAME_OBJECT_ID, GAME_OBJECTS.get(GAME_OBJECT_ID));

        var result = storage.getGameObject(GAME_OBJECT_ID);

        assertEquals(GAME_OBJECTS.get(GAME_OBJECT_ID), result);
    }

    @Test
    void addAndGetActionName() {
        storage.putGameObject(GAME_OBJECT_ID, GAME_OBJECTS.get(GAME_OBJECT_ID));

        storage.addActionName(GAME_OBJECT_ID, ACTION_NAMES.get(ACTION_NAME_ID));

        var result = storage.getActionName(GAME_OBJECT_ID, (long) (ACTION_NAMES.size() - 1));

        assertEquals(ACTION_NAMES.get((long) ACTION_NAMES.size() - 1), result);
    }

    @Test
    void putGameObject() {
        var id = storage.putGameObject(GAME_OBJECTS.get(GAME_OBJECT_ID));

        var result = storage.getGameObject(id);

        assertEquals(GAME_OBJECTS.get(id), result);
    }
}