package ru.otus.architect.ioc.plugins.gen;

import ru.otus.architect.game.Game;
import ru.otus.architect.game.GameObject;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.plugins.IoCPlugin;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GameObjectMovableAdapterPlugin implements IoCPlugin {
    private final BiConsumer<String, FactoryMethod> registerIoCStrategy;
    private final Consumer<String> scopeIoCStrategy;
    private final String scopeName;

    public GameObjectMovableAdapterPlugin(
            BiConsumer<String, FactoryMethod> registerIoCStrategy,
            Consumer<String> scopeIoCStrategy,
            Game game) {
        this.registerIoCStrategy = registerIoCStrategy;
        this.scopeIoCStrategy = scopeIoCStrategy;
        this.scopeName = Optional.ofNullable(game)
                        .map(Game::getGameId)
                        .orElseThrow(RuntimeException::new);
    }

    @Override
    public void execute() {
        scopeIoCStrategy.accept(scopeName);
        registerIoCStrategy.accept("IoC.GameObjectImpl.Movable::getPosition", args -> {
            if (args.length != 1) {
                throw new RuntimeException();
            }
            GameObject obj = (GameObject) args[0];
            return obj.getParameter("position");
        });
        registerIoCStrategy.accept("IoC.GameObjectImpl.Movable::setPosition", args -> {
            if (args.length != 2) {
                throw new RuntimeException();
            }
            GameObject obj = (GameObject) args[0];
            obj.setParameter("position", args[1]);
            return null;
        });
        registerIoCStrategy.accept("IoC.GameObjectImpl.Movable::getVelocity", args -> {
            if (args.length != 1) {
                throw new RuntimeException();
            }
            GameObject obj = (GameObject) args[0];
            return obj.getParameter("velocity");
        });
    }
}
