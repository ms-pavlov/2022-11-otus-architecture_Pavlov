package ru.otus.commands.strategies;

import ru.otus.commands.Command;
import ru.otus.commands.CrossManagerCommand;
import ru.otus.commands.CrossableCommandFactoryImpl;
import ru.otus.commands.CurrentSurroundingCommand;
import ru.otus.commands.stoppable.ChangeableCommand;
import ru.otus.mesh.PlayingField;
import ru.otus.objects.Crossable;

import java.util.function.BiFunction;

public class CurrentSurroundingStrategy implements BiFunction<PlayingField, Crossable, Command> {

    private final Command onCrossCommand;
    private final ChangeableCommand changeableCommand;

    public CurrentSurroundingStrategy(Command onCrossCommand, ChangeableCommand changeableCommand) {
        this.onCrossCommand = onCrossCommand;
        this.changeableCommand = changeableCommand;
    }

    @Override
    public Command apply(PlayingField playingField, Crossable crossable) {
        return new CurrentSurroundingCommand(
                playingField,
                crossable,
                new ChangeSurroundingStrategyImpl(
                        new OldSurroundingHandler(playingField),
                        new NewSurroundingHandler(
                                playingField,
                                changeableCommand,
                                new CrossableCommandFactoryImpl(
                                        (crossable1, crossable2) -> new CrossManagerCommand(crossable1, crossable2, onCrossCommand))
                        )
                )
        );
    }
}
