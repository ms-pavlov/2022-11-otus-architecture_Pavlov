package ru.otus.architect.game;

import org.springframework.stereotype.Component;
import ru.otus.architect.game.objects.dimension.vector.Vector2DBuilder;
import ru.otus.architect.ioc.plugins.IoCPlugin;
import ru.otus.architect.securities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class GameFactoryImpl implements GameFactory {

    private final GamesStorage gamesStorage;
    private final GameObjectStorageFactory gameObjectStorageFactory;
    private final Consumer<Game> gameRegistrationStrategy;

    public GameFactoryImpl(
            GamesStorage gamesStorage,
            GameObjectStorageFactory gameObjectStorageFactory,
            Consumer<Game> gameRegistrationStrategy) {
        this.gamesStorage = gamesStorage;
        this.gameObjectStorageFactory = gameObjectStorageFactory;
        this.gameRegistrationStrategy = gameRegistrationStrategy;
    }


    @Override
    public Game create(List<Function<Game, IoCPlugin>> plugins, List<User> users) {
        GameObjectStorage storage = gameObjectStorageFactory.create();
        Game result = gamesStorage.createGame(storage);

        plugins.forEach(
                plugin -> Optional.of(result)
                        .map(plugin)
                        .ifPresent(IoCPlugin::execute));


        users.forEach(
                user -> {
                    var id = storage.putGameObject(createGameObject());
                    storage.addActionName(id, "Game.MoveCommand");
                    user.addAccess(result.getGameId(), id);
                });
        gameRegistrationStrategy.accept(result);
        return result;
    }

    private GameObject createGameObject() {
        return new GameObjectImpl(
                Map.of(
                        "position", Vector2DBuilder.builder().x(0).y(0).build(),
                        "velocity", Vector2DBuilder.builder().x(1).y(1).build()));
    }
}
