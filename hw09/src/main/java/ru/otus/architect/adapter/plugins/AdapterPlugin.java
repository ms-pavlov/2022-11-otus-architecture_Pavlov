package ru.otus.architect.adapter.plugins;

import ru.otus.architect.adapter.AdapterFactory;
import ru.otus.architect.game.Game;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.plugins.IoCPlugin;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AdapterPlugin implements IoCPlugin {

    private final static String DEPENDENCY_NAME = "Adapter";

    private final BiConsumer<String, FactoryMethod> registerIoCStrategy;
    private final Consumer<String> scopeIoCStrategy;


    private final String scopeName;
    private final AdapterFactory factory;


    public AdapterPlugin(
            BiConsumer<String, FactoryMethod> registerIoCStrategy,
            Consumer<String> scopeIoCStrategy,
            AdapterFactory factory,
            Game game) {
        this.registerIoCStrategy = registerIoCStrategy;
        this.scopeIoCStrategy = scopeIoCStrategy;
        this.scopeName = Optional.ofNullable(game)
                .map(Game::getGameId)
                .orElse(null);
        this.factory = factory;
    }

    @Override
    public void execute() {
        scopeIoCStrategy.accept(scopeName);
        registerIoCStrategy.accept(
                DEPENDENCY_NAME,
                args -> {
                    if (2 != args.length) {
                        throw new RuntimeException();
                    }
                    return factory.getAdapter((Class<?>) args[0], args[1]);
                });
    }
}
