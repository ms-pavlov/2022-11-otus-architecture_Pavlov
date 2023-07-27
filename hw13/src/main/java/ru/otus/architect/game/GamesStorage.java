package ru.otus.architect.game;

import java.util.UUID;

public interface GamesStorage {

    Game getGame(UUID id);

    Game createGame(GameObjectStorage objectStorage);
}
