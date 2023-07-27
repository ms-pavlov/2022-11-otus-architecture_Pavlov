package ru.otus.architect.game;

import ru.otus.architect.ioc.plugins.IoCPlugin;
import ru.otus.architect.securities.User;

import java.util.List;
import java.util.function.Function;

public interface GameFactory {

    Game create(
            List<Function<Game, IoCPlugin>> plugin,
            List<User> users);

}
