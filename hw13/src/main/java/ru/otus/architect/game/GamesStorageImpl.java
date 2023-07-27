package ru.otus.architect.game;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class GamesStorageImpl implements GamesStorage {

    private final Map<UUID, Game> games;
    private final Supplier<UUID> idGenerator;

    public GamesStorageImpl(
            Map<UUID, Game> games,
            Supplier<UUID> idGenerator) {
        this.games = games;
        this.idGenerator = idGenerator;
    }

    public GamesStorageImpl() {
        this(new HashMap<>(), UUID::randomUUID);
    }

    @Override
    public Game getGame(UUID id) {
        return games.get(id);
    }

    @Override
    public Game createGame(GameObjectStorage objectStorage) {
        var id = idGenerator.get();
        var result = new GameImpl(id, objectStorage);
        games.put(id, result);
        return result;
    }
}
