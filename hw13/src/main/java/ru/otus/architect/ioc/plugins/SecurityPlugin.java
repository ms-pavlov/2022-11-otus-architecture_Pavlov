package ru.otus.architect.ioc.plugins;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import ru.otus.architect.game.Game;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.securities.KeyService;
import ru.otus.openapi.model.Message;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SecurityPlugin implements IoCPlugin {

    private final BiConsumer<String, FactoryMethod> registerIoCStrategy;
    private final Consumer<String> scopeIoCStrategy;
    private final KeyService keyService;
    private final String scopeName;

    public SecurityPlugin(
            BiConsumer<String, FactoryMethod> registerIoCStrategy,
            Consumer<String> scopeIoCStrategy,
            KeyService keyService,
            Game game) {
        this.registerIoCStrategy = registerIoCStrategy;
        this.scopeIoCStrategy = scopeIoCStrategy;
        this.keyService = keyService;
        this.scopeName = Optional.ofNullable(game)
                .map(Game::getGameId)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void execute() {
        scopeIoCStrategy.accept(scopeName);
        registerIoCStrategy.accept(
                "User.GameObject::hasAccess",
                args -> {
                    if (args.length != 1) {
                        throw new RuntimeException();
                    }
                    Message message = (Message) args[0];
                    try {
                        var jwt = Jwts.parserBuilder()
                                .setSigningKey(keyService.getPublic())
                                .build()
                                .parse(message.getToken());


                        Claims body = (Claims) jwt.getBody();


                        List<Long> accesses = ((List<Integer>) body.get("accesses")).stream()
                                .map(Long::valueOf)
                                .toList();

                        return Optional.ofNullable(body.get("game", String.class))
                                       .map(message.getGame().toString()::equals)
                                       .orElse(false) &&
                               accesses.contains(message.getGameObject());
                    } catch (SignatureException signatureException) {
                        return false;
                    }
                });
    }
}
