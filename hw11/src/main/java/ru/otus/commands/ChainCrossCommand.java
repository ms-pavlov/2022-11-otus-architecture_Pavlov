package ru.otus.commands;

import ru.otus.commands.macrocommands.TransactionalMacroCommandChange;
import ru.otus.mesh.PlayingField;
import ru.otus.objects.Crossable;

import java.util.List;
import java.util.function.BiFunction;

public class ChainCrossCommand implements Command {

    private final Command command;

    public ChainCrossCommand(
            PlayingField field,
            Crossable crossable,
            BiFunction<PlayingField, Crossable, Command> crossCalculationStrategy,
            BiFunction<PlayingField, Crossable, Command> currentSurroundingStrategy) {

        this.command = new TransactionalMacroCommandChange(
                List.of(
                        currentSurroundingStrategy.apply(field, crossable),
                        crossCalculationStrategy.apply(field, crossable)
                )
        );
    }

    @Override
    public void execute() {
        command.execute();
    }
}
