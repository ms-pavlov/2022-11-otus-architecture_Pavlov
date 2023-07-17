package ru.otus.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.architect.adapter.AdapterFactory;
import ru.otus.architect.adapter.plugins.AdapterPlugin;
import ru.otus.architect.commands.CommandFactory;
import ru.otus.architect.game.Game;
import ru.otus.architect.game.GameBuilder;
import ru.otus.architect.game.GameObjectImpl;
import ru.otus.architect.game.GameObjectStorageImpl;
import ru.otus.architect.game.objects.dimension.vector.Vector2DBuilder;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.plugins.CommandsPlugin;
import ru.otus.architect.ioc.plugins.GameRegistrationPlugin;
import ru.otus.architect.ioc.plugins.gen.GameObjectMovableAdapterPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Component
public class PluginInitializingBean implements InitializingBean {

    private final BiConsumer<String, FactoryMethod> registerIoCStrategy;
    private final Consumer<String> scopeIoCStrategy;
    private final AdapterFactory adapterFactory;
    private final CommandFactory commandFactory;
    private final Consumer<Game> gameRegistrationStrategy;

    public PluginInitializingBean(
            @Qualifier("registerIoCStrategy") BiConsumer<String, FactoryMethod> registerIoCStrategy,
            @Qualifier("scopeIoCStrategy") Consumer<String> scopeIoCStrategy,
            AdapterFactory adapterFactory,
            CommandFactory commandFactory,
            Consumer<Game> gameRegistrationStrategy) {
        this.registerIoCStrategy = registerIoCStrategy;
        this.scopeIoCStrategy = scopeIoCStrategy;
        this.adapterFactory = adapterFactory;
        this.commandFactory = commandFactory;
        this.gameRegistrationStrategy = gameRegistrationStrategy;
    }

    @Override
    public void afterPropertiesSet() {
        gameRegistrationStrategy.accept(
                GameBuilder.builder(new GameObjectStorageImpl())
                        .id(UUID.fromString("a8bc388b-905e-462b-bb6e-d6c2693fcf5f"))
                        .addPlugin(value -> new GameObjectMovableAdapterPlugin(registerIoCStrategy, scopeIoCStrategy, value))
                        .addPlugin(value -> new AdapterPlugin(registerIoCStrategy, scopeIoCStrategy, adapterFactory, value))
                        .addPlugin(value -> new GameRegistrationPlugin(registerIoCStrategy, scopeIoCStrategy, value))
                        .addPlugin(value -> new CommandsPlugin(registerIoCStrategy, scopeIoCStrategy, commandFactory, value))
                        .addGameObject(
                                1L,
                                new GameObjectImpl(
                                        Map.of(
                                                "position", Vector2DBuilder.builder().x(0).y(0).build(),
                                                "velocity", Vector2DBuilder.builder().x(1).y(1).build())))
                        .addActionName(1L, "Game.MoveCommand")
                        .build());
    }
}
