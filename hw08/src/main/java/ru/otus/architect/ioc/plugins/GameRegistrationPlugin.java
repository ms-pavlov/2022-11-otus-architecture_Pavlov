package ru.otus.architect.ioc.plugins;

import ru.otus.architect.commands.Command;
import ru.otus.architect.game.Game;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.openapi.model.Message;

import java.util.Optional;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GameRegistrationPlugin implements IoCPlugin {
    private final BiConsumer<String, FactoryMethod> registerIoCStrategy;
    private final Consumer<String> scopeIoCStrategy;
    private final Game game;

    public GameRegistrationPlugin(
            BiConsumer<String, FactoryMethod> registerIoCStrategy,
            Consumer<String> scopeIoCStrategy,
            Game game) {
        this.registerIoCStrategy = registerIoCStrategy;
        this.scopeIoCStrategy = scopeIoCStrategy;
        this.game = game;
    }

    @Override
    public void execute() {
        scopeIoCStrategy.accept(
                Optional.ofNullable(game)
                        .map(Game::getGameId)
                        .orElseThrow(RuntimeException::new));

        registerIoCStrategy.accept(
                "IoC.Game",
                args -> Optional.of(game)
                        .orElseThrow(RuntimeException::new));
        registerIoCStrategy.accept(
                "IoC.Game::addCommand",
                args -> {
                    if (args.length != 1) {
                        throw new RuntimeException();
                    }
                    return game.addCommand((Command) args[0]);
                });
        registerIoCStrategy.accept(
                "IoC.Game::pollCommand",
                args -> game.pollCommand());
        registerIoCStrategy.accept(
                "IoC.Game::getGameObject",
                args -> {
                    if (args.length != 1) {
                        throw new RuntimeException();
                    }
                    Message msg = (Message) Optional.ofNullable(args[0])
                            .orElseThrow(RuntimeException::new);
                    return Optional.of(game)
                            .map(value -> value.getGameObject(msg.getGameObject()))
                            .orElseThrow(RuntimeException::new);
                });
        registerIoCStrategy.accept(
                "IoC.Game::getActionName",
                args -> {
                    if (args.length != 1) {
                        throw new RuntimeException();
                    }
                    Message msg = (Message) Optional.ofNullable(args[0])
                            .orElseThrow(RuntimeException::new);
                    return Optional.of(game)
                            .map(value -> value.getActionName(msg.getGameObject(), msg.getAction()))
                            .orElseThrow(RuntimeException::new);
                });
    }

}
