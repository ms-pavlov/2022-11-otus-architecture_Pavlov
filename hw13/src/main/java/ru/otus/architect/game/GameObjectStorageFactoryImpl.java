package ru.otus.architect.game;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class GameObjectStorageFactoryImpl implements GameObjectStorageFactory {

    private final Supplier<GameObjectStorage> gameObjectStorageInitStrategy;


    public GameObjectStorageFactoryImpl(Supplier<GameObjectStorage> gameObjectStorageInitStrategy) {
        this.gameObjectStorageInitStrategy = gameObjectStorageInitStrategy;
    }

    @Override
    public GameObjectStorage create() {
        return gameObjectStorageInitStrategy.get();
    }
}
