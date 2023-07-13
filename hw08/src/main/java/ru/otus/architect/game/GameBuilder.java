package ru.otus.architect.game;

import ru.otus.architect.ioc.plugins.IoCPlugin;

import java.util.*;
import java.util.function.Function;

public class GameBuilder {

    private final List<Function<Game, IoCPlugin>> plugins;
    private final GameObjectStorage objectStorage;

    private UUID gameId;


    private GameBuilder(GameObjectStorage objectStorage) {
        this.plugins = new ArrayList<>();
        this.objectStorage = objectStorage;
    }

    public static GameBuilder builder(GameObjectStorage objectStorage) {
        return new GameBuilder(objectStorage);
    }

    public GameBuilder id(UUID gameId) {
        this.gameId = gameId;
        return this;
    }

    public GameBuilder addPlugin(Function<Game, IoCPlugin> plugin) {
        plugins.add(plugin);
        return this;
    }

    public GameBuilder addGameObject(Long id, GameObject gameObject) {
        this.objectStorage.putGameObject(id, gameObject);
        return this;
    }

    public GameBuilder addActionName(Long objectId, String actionName) {
        this.objectStorage.addActionName(objectId, actionName);
        return this;
    }

    public Game build() {
        if (null == gameId) {
            gameId = UUID.randomUUID();
        }

        var result = new GameImpl(gameId, objectStorage, new LinkedList<>());

        plugins.forEach(
                plugin -> Optional.of(result)
                        .map(plugin)
                        .ifPresent(IoCPlugin::execute));

        return new GameImpl(gameId, objectStorage, new LinkedList<>());
    }
}
