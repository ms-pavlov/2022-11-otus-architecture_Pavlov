package ru.otus.architect.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameImplTest {

    private final static UUID GAME_UUID = UUID.randomUUID();
    private final static Long GAME_OBJECT_ID = 1L;
    private final static Map<Long, GameObject> GAME_OBJECTS = Map.of(GAME_OBJECT_ID, new GameObjectImpl());
    private final static Long ACTION_NAME_ID = 1L;
    private final static Map<Long, String> ACTION_NAMES = Map.of(ACTION_NAME_ID, "test");


    @Mock
    private GameObjectStorage objectStorage;

    @Mock
    private Queue<Command> commandQueue;
    @Mock
    private Command command;

    private Game game;

    @BeforeEach
    void setUp() {
        game = new GameImpl(GAME_UUID, objectStorage, commandQueue);
    }

    @Test
    void getGameId() {

        String result = game.getGameId();

        assertEquals(GAME_UUID.toString(), result);
    }

    @Test
    void addCommand() {
        when(commandQueue.offer(any())).thenReturn(true);

        assertTrue(game.addCommand(command));

        verify(commandQueue, times(1)).offer(command);
    }

    @Test
    void pollCommand() {
        when(commandQueue.poll()).thenReturn(command);

        var result = game.pollCommand();

        assertEquals(command, result);
        verify(commandQueue, times(1)).poll();
    }

    @Test
    void getGameObject() {
        when(objectStorage.getGameObject(GAME_OBJECT_ID)).thenReturn(GAME_OBJECTS.get(GAME_OBJECT_ID));

        var result = game.getGameObject(GAME_OBJECT_ID);

        assertEquals(GAME_OBJECTS.get(GAME_OBJECT_ID), result);
        verify(objectStorage, times(1)).getGameObject(GAME_OBJECT_ID);
    }

    @Test
    void getActionName() {
        when(objectStorage.getActionName(GAME_OBJECT_ID, ACTION_NAME_ID)).thenReturn(ACTION_NAMES.get(ACTION_NAME_ID));

        var result = game.getActionName(GAME_OBJECT_ID, ACTION_NAME_ID);

        assertEquals(ACTION_NAMES.get(ACTION_NAME_ID), result);
        verify(objectStorage, times(1)).getActionName(GAME_OBJECT_ID, ACTION_NAME_ID);
    }

    @Test
    void getAllGameObjects() {
        when(objectStorage.getAllGameObjects()).thenReturn(GAME_OBJECTS);

        var result = game.getAllGameObjects();

        assertEquals(GAME_OBJECTS, result);
        verify(objectStorage, times(1)).getAllGameObjects();
    }

    @Test
    void getGameObjectsActionNames() {
        when(objectStorage.getGameObjectsActionNames(GAME_OBJECT_ID)).thenReturn(ACTION_NAMES);

        var result = game.getGameObjectsActionNames(GAME_OBJECT_ID);

        assertEquals(ACTION_NAMES, result);
        verify(objectStorage, times(1)).getGameObjectsActionNames(GAME_OBJECT_ID);
    }
}