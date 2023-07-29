package ru.otus.commands;

import ru.otus.commands.strategies.ChangeSurroundingStrategy;
import ru.otus.mesh.PlayingField;
import ru.otus.objects.Crossable;

import java.util.ArrayList;
import java.util.Optional;

public class CurrentSurroundingCommand implements Command {

    private final PlayingField field;
    private final Crossable crossable;
    private final ChangeSurroundingStrategy changeSurroundingStrategy;

    public CurrentSurroundingCommand(
            PlayingField field,
            Crossable crossable,
            ChangeSurroundingStrategy changeSurroundingStrategy) {
        this.field = field;
        this.crossable = crossable;
        this.changeSurroundingStrategy = changeSurroundingStrategy;
    }


    @Override
    public void execute() {
        var surrounding = Optional.ofNullable(field)
                .map(PlayingField::getSurroundings)
                .orElseGet(ArrayList::new)
                .stream()
                .filter(value -> value.contains(crossable))
                .findFirst()
                .orElse(null);

        if (crossable.getSurrounding(field) != surrounding) {
            changeSurroundingStrategy.accept(crossable, surrounding);
        }
    }
}
