package ru.otus.architect.game;

import java.util.*;
import java.util.stream.Collectors;

public class GameObjectStorageImpl implements GameObjectStorage {


    private final Map<Long, GameObjectInfo> storage;

    public GameObjectStorageImpl() {
        storage = new HashMap<>();
    }

    @Override
    public GameObject getGameObject(Long id) {
        return Optional.ofNullable(id)
                .map(storage::get)
                .map(GameObjectInfo::getGameObject)
                .orElse(null);
    }

    @Override
    public String getActionName(Long objectId, Long actionId) {
        return Optional.ofNullable(objectId)
                .map(storage::get)
                .map(item -> item.getActionName(actionId))
                .orElse(null);
    }

    @Override
    public Map<Long, GameObject> getAllGameObjects() {
        return storage.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        item -> Optional.of(item)
                                .map(Map.Entry::getValue)
                                .map(GameObjectInfo::getGameObject)
                                .orElseGet(NullGameObject::new)));
    }

    @Override
    public Map<Long, String> getGameObjectsActionNames(Long objectId) {
        List<String> names = Optional.ofNullable(objectId)
                .map(storage::get)
                .map(GameObjectInfo::getActionNames)
                .orElseGet(ArrayList::new);
        return names.stream()
                .filter(Objects::nonNull)
                .collect(
                        Collectors.toMap(
                                item -> Optional.of(item)
                                        .map(names::indexOf)
                                        .map(Long::valueOf)
                                        .orElse(null),
                                item -> item));
    }

    @Override
    public void putGameObject(Long objectId, GameObject gameObject) {
        storage.put(objectId, new GameObjectInfo(gameObject));
    }

    @Override
    public void addActionName(Long objectId, String actionName) {
        Optional.ofNullable(objectId)
                .map(storage::get)
                .ifPresent(item -> item.addActionNames(actionName));
    }

    static class GameObjectInfo {
        private final GameObject gameObject;
        private final List<String> actionNames;

        public GameObjectInfo(GameObject gameObject) {
            this.gameObject = gameObject;
            this.actionNames = new ArrayList<>();
        }

        private GameObject getGameObject() {
            return gameObject;
        }

        private List<String> getActionNames() {
            return actionNames;
        }

        private String getActionName(Long id) {
            return Optional.ofNullable(id)
                    .filter(value -> actionNames.size() > value && 0 >= value)
                    .map(Long::intValue)
                    .map(actionNames::get)
                    .orElse(null);
        }

        private void addActionNames(String name) {
            this.actionNames.add(name);
        }
    }
}
