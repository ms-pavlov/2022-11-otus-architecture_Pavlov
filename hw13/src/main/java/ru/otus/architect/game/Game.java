package ru.otus.architect.game;

import ru.otus.architect.commands.Command;

import java.util.Map;
import java.util.Queue;

public interface Game {

    String getGameId();

    boolean addCommand(Command command);

    Command pollCommand();

    GameObject getGameObject(Long objectId);

    String getActionName(Long objectId, Long actionId);

    Map<Long, GameObject> getAllGameObjects();

    Map<Long, String> getGameObjectsActionNames(Long objectId);
 }
