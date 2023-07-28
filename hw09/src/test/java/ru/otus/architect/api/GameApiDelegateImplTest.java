package ru.otus.architect.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.otus.architect.game.Game;
import ru.otus.architect.game.GameFactory;
import ru.otus.architect.ioc.plugins.IoCPlugin;
import ru.otus.architect.securities.TokenFactory;
import ru.otus.architect.securities.User;
import ru.otus.architect.securities.UsersService;
import ru.otus.openapi.api.GameApiDelegate;
import ru.otus.openapi.model.GameRequest;
import ru.otus.openapi.model.GameResponse;
import ru.otus.openapi.model.TokenResponse;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameApiDelegateImplTest {

    private final static String USER_NAME = "test";
    private final static String TOKEN = "test2";
    private final static User USER = new User(USER_NAME, "pass");
    private final static UUID GAME_UUID = UUID.randomUUID();
    @Mock
    private UsersService usersService;
    @Mock
    private GameFactory gameFactory;
    @Mock
    private TokenFactory tokenFactory;
    @Mock
    private List<Function<Game, IoCPlugin>> defaultGamePlugins;
    @Mock
    private GameRequest gameRequest;
    @Mock
    private ServerWebExchange exchange;
    @Mock
    private Principal principal;
    @Mock
    private Game game;
    private GameApiDelegate delegate;


    @BeforeEach
    void setUp() {
        delegate = new GameApiDelegateImpl(usersService, gameFactory, tokenFactory, defaultGamePlugins);
    }

    @Test
    @DisplayName("Создает новую игру и возвращает данные о созданной игре")
    void create() {
        when(exchange.getPrincipal()).thenReturn(Mono.just(principal));
        when(principal.getName()).thenReturn(USER_NAME);
        when(usersService.getUser(USER_NAME)).thenReturn(USER);
        when(gameRequest.getUsers()).thenReturn(List.of(USER_NAME));
        when(gameFactory.create(any(), any())).thenReturn(game);
        when(game.getGameId()).thenReturn(GAME_UUID.toString());

        var result = delegate.create(Mono.just(gameRequest), exchange).block();

        assertEquals(
                List.of(USER_NAME),
                Optional.ofNullable(result)
                        .map(ResponseEntity::getBody)
                        .map(GameResponse::getUsers)
                        .orElse(null));
        assertEquals(
                USER_NAME,
                Optional.ofNullable(result)
                        .map(ResponseEntity::getBody)
                        .map(GameResponse::getAuthor)
                        .orElse(null));
        assertEquals(
                GAME_UUID,
                Optional.ofNullable(result)
                        .map(ResponseEntity::getBody)
                        .map(GameResponse::getGame)
                        .orElse(null));

        verify(gameFactory, times(1)).create(defaultGamePlugins, List.of(USER));
    }

    @Test
    @DisplayName("Создает токен")
    void getToken() {
        when(exchange.getPrincipal()).thenReturn(Mono.just(principal));
        when(principal.getName()).thenReturn(USER_NAME);
        when(usersService.getUser(USER_NAME)).thenReturn(USER);
        when(tokenFactory.create(GAME_UUID, USER)).thenReturn(TOKEN);

        var result = delegate.getToken(GAME_UUID, exchange).block();

        assertEquals(
                TOKEN,
                Optional.ofNullable(result)
                        .map(ResponseEntity::getBody)
                        .map(TokenResponse::getToken)
                        .orElse(null));

        assertEquals(
                GAME_UUID,
                Optional.ofNullable(result)
                        .map(ResponseEntity::getBody)
                        .map(TokenResponse::getGame)
                        .orElse(null));

        verify(tokenFactory, times(1)).create(GAME_UUID, USER);
    }
}