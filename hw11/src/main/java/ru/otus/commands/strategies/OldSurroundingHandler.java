package ru.otus.commands.strategies;

import ru.otus.mesh.PlayingField;
import ru.otus.objects.Crossable;

import java.util.function.Consumer;

public class OldSurroundingHandler implements Consumer<Crossable> {

    private final PlayingField field;

    public OldSurroundingHandler(PlayingField field) {
        this.field = field;
    }

    @Override
    public void accept(Crossable crossable) {
        crossable.getSurrounding(field).remove(crossable);
    }
}
