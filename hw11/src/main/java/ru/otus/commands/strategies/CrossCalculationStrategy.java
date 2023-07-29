package ru.otus.commands.strategies;

import ru.otus.commands.Command;
import ru.otus.commands.CrossableCommandFactory;
import ru.otus.commands.macrocommands.TransactionalMacroCommandChange;
import ru.otus.commands.stoppable.ChangeableCommand;
import ru.otus.mesh.PlayingField;
import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;

public class CrossCalculationStrategy implements BiFunction<PlayingField, Crossable, Command> {

    private final CrossableCommandFactory crossableCommandFactory;

    public CrossCalculationStrategy(CrossableCommandFactory crossableCommandFactory) {
        this.crossableCommandFactory = crossableCommandFactory;
    }


    @Override
    public Command apply(PlayingField field, Crossable crossable) {
        return new TransactionalMacroCommandChange(
                crossableCommandFactory.apply(crossable, crossable.getSurrounding(field))
        );
    }
}
