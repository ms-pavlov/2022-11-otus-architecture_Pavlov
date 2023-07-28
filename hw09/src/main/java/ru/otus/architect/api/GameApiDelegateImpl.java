package ru.otus.architect.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class GameApiDelegateImpl implements GameApiDelegate {

    private final UsersService usersService;
    private final GameFactory gameFactory;
    private final TokenFactory tokenFactory;
    private final List<Function<Game, IoCPlugin>> defaultGamePlugins;

    public GameApiDelegateImpl(
            UsersService usersService,
            GameFactory gameFactory,
            TokenFactory tokenFactory,
            @Qualifier("defaultGamePlugins") List<Function<Game, IoCPlugin>> defaultGamePlugins) {
        this.usersService = usersService;
        this.gameFactory = gameFactory;
        this.tokenFactory = tokenFactory;
        this.defaultGamePlugins = defaultGamePlugins;
    }

    @Override
    public Mono<ResponseEntity<GameResponse>> create(Mono<GameRequest> gameRequest, ServerWebExchange exchange) {
        return gameRequest.flatMap(
                value -> exchange.getPrincipal()
                        .flatMap(
                                principal -> {
                                    var users = value.getUsers().stream()
                                            .map(usersService::getUser)
                                            .filter(Objects::nonNull)
                                            .toList();
                                    var game = gameFactory.create(defaultGamePlugins, users);
                                    return Mono.just(
                                            ResponseEntity.ok(
                                                    new GameResponse()
                                                            .author(principal.getName())
                                                            .users(
                                                                    users.stream()
                                                                            .map(User::username)
                                                                            .toList())
                                                            .game(UUID.fromString(game.getGameId()))));
                                }
                        ));
    }

    @Override
    public Mono<ResponseEntity<TokenResponse>> getToken(UUID uuid, ServerWebExchange exchange) {
        return exchange.getPrincipal()
                .flatMap(
                        principal -> Mono.just(
                                Optional.ofNullable(principal)
                                        .map(Principal::getName)
                                        .map(usersService::getUser)
                                        .map(user -> new TokenResponse()
                                                .game(uuid)
                                                .token(tokenFactory.create(uuid, user)))
                                        .map(ResponseEntity::ok)
                                        .orElseGet(() -> ResponseEntity.badRequest().build())));
    }
}
