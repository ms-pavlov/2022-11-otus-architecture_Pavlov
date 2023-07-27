package ru.otus.architect.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.ioc.plugins.IoCPlugin;
import ru.otus.architect.securities.User;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameFactoryImplTest {


    private final static String USER_NAME = "test";
    private final static String GAME_UUID = UUID.randomUUID().toString();
    @Mock
    private GamesStorage gamesStorage;
    @Mock
    private GameObjectStorageFactory gameObjectStorageFactory;
    @Mock
    private GameObjectStorage storage;
    @Mock
    private Function<Game, IoCPlugin> plugin;
    @Mock
    private Game game;
    @Mock
    private Consumer<Game> gameRegistrationStrategy;

    private List<User> users;
    private GameFactory factory;

    @BeforeEach
    void setUp() {
        factory = new GameFactoryImpl(gamesStorage, gameObjectStorageFactory, gameRegistrationStrategy);
        users = List.of(new User(USER_NAME, ""));
    }


    @Test
    void create() {
        when(gameObjectStorageFactory.create()).thenReturn(storage);
        when(gamesStorage.createGame(storage)).thenReturn(game);
        when(game.getGameId()).thenReturn(GAME_UUID);

        var result = factory.create(List.of(plugin), users);

        assertEquals(game, result);

        assertEquals(
                1,
                users.stream().findFirst()
                        .map(user -> user.getGameAccesses(result.getGameId()))
                        .map(List::size)
                        .orElse(null));

        verify(gameObjectStorageFactory, times(1)).create();
        verify(gameRegistrationStrategy, times(1)).accept(game);
        verify(gamesStorage, times(1)).createGame(storage);
    }

}