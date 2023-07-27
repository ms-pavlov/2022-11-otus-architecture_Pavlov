package ru.otus.architect.ioc.plugins;

import ru.otus.architect.api.AnswerConsumer;
import ru.otus.architect.commands.CommandFactory;
import ru.otus.architect.game.Game;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.openapi.model.Message;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommandsPlugin implements IoCPlugin {

    private final BiConsumer<String, FactoryMethod> registerIoCStrategy;
    private final Consumer<String> scopeIoCStrategy;
    private final CommandFactory commandFactory;
    private final String scopeName;


    public CommandsPlugin(
            BiConsumer<String, FactoryMethod> registerIoCStrategy,
            Consumer<String> scopeIoCStrategy,
            CommandFactory commandFactory,
            Game game) {
        this.registerIoCStrategy = registerIoCStrategy;
        this.scopeIoCStrategy = scopeIoCStrategy;
        this.commandFactory = commandFactory;
        this.scopeName = Optional.ofNullable(game)
                .map(Game::getGameId)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void execute() {
        scopeIoCStrategy.accept(scopeName);
        registerIoCStrategy.accept(
                "Game.MoveCommand",
                args -> {
                    if (args.length != 2) {
                        throw new RuntimeException();
                    }
                    return commandFactory.create("Game.MoveCommand", (Message) args[0], (AnswerConsumer) args[1]);
                });
        registerIoCStrategy.accept(
                "Message.InterpretCommand",
                args -> {
                    if (args.length != 2) {
                        throw new RuntimeException();
                    }
                    return commandFactory.create("Message.InterpretCommand", (Message) args[0], (AnswerConsumer) args[1]);
                });
    }
}
