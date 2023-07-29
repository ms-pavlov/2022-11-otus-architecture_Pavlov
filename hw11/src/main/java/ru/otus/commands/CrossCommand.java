package ru.otus.commands;

import ru.otus.commands.macrocommands.TransactionalMacroCommandChange;
import ru.otus.mesh.PlayingField;
import ru.otus.objects.Crossable;

import java.util.List;
import java.util.function.BiFunction;

public class CrossCommand implements Command {

    private final Command command;

    public CrossCommand(
            List<PlayingField> fields,
            Crossable crossable,
            BiFunction<PlayingField, Crossable, Command> chainCrossCommandStrategy) {
        command = new TransactionalMacroCommandChange(
                fields.stream()
                        .map(field -> chainCrossCommandStrategy.apply(field, crossable))
                        .toList()
        );
    }

    @Override
    public void execute() {
        command.execute();
    }
}
