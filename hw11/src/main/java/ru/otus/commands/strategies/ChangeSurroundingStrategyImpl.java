package ru.otus.commands.strategies;

import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChangeSurroundingStrategyImpl implements ChangeSurroundingStrategy{

    public final Consumer<Crossable> oldSurroundingHandler;
    public final BiConsumer<Crossable, Surrounding> newSurroundingHandler;

    public ChangeSurroundingStrategyImpl(Consumer<Crossable> oldSurroundingHandler, BiConsumer<Crossable, Surrounding> newSurroundingHandler) {
        this.oldSurroundingHandler = oldSurroundingHandler;
        this.newSurroundingHandler = newSurroundingHandler;
    }

    @Override
    public void accept(Crossable crossable, Surrounding surrounding) {
        oldSurroundingHandler.accept(crossable);
        newSurroundingHandler.accept(crossable, surrounding);
    }
}
