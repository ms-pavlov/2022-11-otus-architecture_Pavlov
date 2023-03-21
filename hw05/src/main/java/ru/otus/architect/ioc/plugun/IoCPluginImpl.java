package ru.otus.architect.ioc.plugun;

import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.MoveCommand;
import ru.otus.architect.commands.macro.MacroCommand;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.methods.FactoryMethod;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IoCPluginImpl implements IoCPlugin {

    private final String registerName;
    private final String scopeNewName;
    private final IoCContainer container;

    public IoCPluginImpl(String registerName, String scopeNewName, IoCContainer container) {
        this.registerName = registerName;
        this.scopeNewName = scopeNewName;
        this.container = container;
    }

    @Override
    public void execute() {
        container.resolve(scopeNewName, "Scope1");
        container.resolve(registerName, "MoveCommand",
                (FactoryMethod) args -> new MoveCommand((Movable) args[0]));
        container.resolve(registerName, "MoveMacroCommand",
                (FactoryMethod) args -> new MacroCommand(
                        Stream.of(args)
                                .map(arg -> (Command) new MoveCommand((Movable) arg))
                                .toList()));
    }
}
