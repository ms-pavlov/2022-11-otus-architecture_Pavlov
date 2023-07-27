package ru.otus.architect.ioc.plugins;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.Game;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.securities.KeyService;
import ru.otus.architect.securities.KeyServiceImpl;
import ru.otus.architect.securities.User;
import ru.otus.openapi.model.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityPluginTest {

    private final static UUID GAME_UUID = UUID.randomUUID();
    private final static List<Long> ACCESSES = List.of(1L);
    private final static User USER = new User(
            "test",
            "pass",
            Map.of(GAME_UUID.toString(), ACCESSES));
    private final static KeyService KEY_SERVICE = new KeyServiceImpl();
    private final static String TEST_TOKEN = Jwts.builder()
            .signWith(KEY_SERVICE.getPrivate())
            .claim("accesses", USER.getGameAccesses(GAME_UUID.toString()))
            .claim("game", GAME_UUID.toString())
            .compact();
    private final static String BAD_TOKEN = Jwts.builder()
            .signWith(Keys.keyPairFor(SignatureAlgorithm.RS512).getPrivate())
            .claim("accesses", USER.getGameAccesses(GAME_UUID.toString()))
            .claim("game", GAME_UUID.toString())
            .compact();

    @Mock
    private BiConsumer<String, FactoryMethod> registerIoCStrategy;
    @Mock
    private Consumer<String> scopeIoCStrategy;
    @Mock
    private Game game;
    @Mock
    private Message message;

    private IoCPlugin plugin;
    private Map<String, FactoryMethod> storage;

    @BeforeEach
    void setUp() {
        storage = new HashMap<>();
        when(game.getGameId()).thenReturn(GAME_UUID.toString());
        doAnswer(
                invocationOnMock -> storage.put(
                        invocationOnMock.getArgument(0),
                        invocationOnMock.getArgument(1)))
                .when(registerIoCStrategy)
                .accept(any(), any());
        plugin = new SecurityPlugin(registerIoCStrategy, scopeIoCStrategy, KEY_SERVICE, game);
    }

    @Test
    void execute() {
        plugin.execute();

        verify(scopeIoCStrategy, times(1)).accept(eq(GAME_UUID.toString()));
        verify(registerIoCStrategy, times(1)).accept(eq("User.GameObject::hasAccess"), any());
    }

    @Test
    @DisplayName("Зависимость User.GameObject::hasAccess проверяет права доступа")
    void hasAccess() {
        when(message.getToken()).thenReturn(TEST_TOKEN);
        when(message.getGameObject()).thenReturn(ACCESSES.get(0));
        when(message.getGame()).thenReturn(GAME_UUID);

        plugin.execute();

        var result = storage.get("User.GameObject::hasAccess");

        assertTrue((boolean) result.create(message));
    }

    @Test
    @DisplayName("Зависимость User.GameObject::hasAccess проверяет права доступа")
    void hasNoAccess() {
        when(message.getToken()).thenReturn(TEST_TOKEN);
        when(message.getGameObject()).thenReturn(2L);
        when(message.getGame()).thenReturn(GAME_UUID);

        plugin.execute();

        var result = storage.get("User.GameObject::hasAccess");

        assertFalse((boolean) result.create(message));
    }

    @Test
    @DisplayName("Зависимость User.GameObject::hasAccess возврашает false если токен не валиден")
    void testInvalidToken() {
        when(message.getToken()).thenReturn(BAD_TOKEN);

        plugin.execute();

        var result = storage.get("User.GameObject::hasAccess");

        assertFalse((boolean) result.create(message));
    }

    @Test
    @DisplayName("Зависимость User.GameObject::hasAccess возврашает false если токен для другой игры")
    void testOtherGameToken() {
        when(message.getToken()).thenReturn(TEST_TOKEN);
        when(message.getGame()).thenReturn(UUID.randomUUID());

        plugin.execute();

        var result = storage.get("User.GameObject::hasAccess");

        assertFalse((boolean) result.create(message));
    }
}