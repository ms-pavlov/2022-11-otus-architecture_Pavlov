package ru.otus.architect.game;

import ru.otus.architect.commands.Command;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

public class GameImpl implements Game {

    private final UUID uuid;
    private final GameObjectStorage objectStorage;
    private final Queue<Command> commandQueue;

    public GameImpl(UUID uuid, GameObjectStorage objectStorage, Queue<Command> commandQueue) {
        this.uuid = uuid;
        this.objectStorage = objectStorage;
        this.commandQueue = commandQueue;
    }

    @Override
    public String getGameId() {
        return uuid.toString();
    }

    @Override
    public boolean addCommand(@NotNull Command command) {
        return commandQueue.offer(command);
    }

    @Override
    public Command pollCommand() {
        return commandQueue.poll();
    }

    @Override
    public GameObject getGameObject(Long objectId) {
        return objectStorage.getGameObject(objectId);
    }

    @Override
    public String getActionName(Long objectId, Long actionId) {
        return objectStorage.getActionName(objectId, actionId);
    }

    @Override
    public Map<Long, GameObject> getAllGameObjects() {
        return objectStorage.getAllGameObjects();
    }

    @Override
    public Map<Long, String> getGameObjectsActionNames(Long objectId) {
        return objectStorage.getGameObjectsActionNames(objectId);
    }
}
