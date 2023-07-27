package ru.otus.architect.game;

import java.util.Map;

public interface GameObjectStorage {

    GameObject getGameObject(Long id);

    String getActionName(Long objectId, Long actionId);

    Map<Long, GameObject> getAllGameObjects();

    Map<Long, String> getGameObjectsActionNames(Long objectId);

    void putGameObject(Long objectId, GameObject gameObject);

    Long putGameObject(GameObject gameObject);

    void addActionName(Long objectId, String actionName);
}
