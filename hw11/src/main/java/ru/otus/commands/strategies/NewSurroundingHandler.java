package ru.otus.commands.strategies;

import ru.otus.commands.CrossableCommandFactory;
import ru.otus.commands.macrocommands.TransactionalMacroCommandChange;
import ru.otus.commands.stoppable.ChangeableCommand;
import ru.otus.mesh.PlayingField;
import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public class NewSurroundingHandler implements BiConsumer<Crossable, Surrounding> {

    private final PlayingField field;
    private final ChangeableCommand changeableCommand;
    private final CrossableCommandFactory crossableCommandFactory;

    public NewSurroundingHandler(
            PlayingField field,
            ChangeableCommand changeableCommand,
            CrossableCommandFactory crossableCommandFactory) {
        this.field = field;
        this.changeableCommand = changeableCommand;
        this.crossableCommandFactory = crossableCommandFactory;
    }


    @Override
    public void accept(Crossable crossable, Surrounding surrounding) {
        crossable.setSurrounding(field, surrounding);
        changeableCommand.changeCommand(
                new TransactionalMacroCommandChange(
                        crossableCommandFactory.apply(crossable, surrounding)));
    }
}
