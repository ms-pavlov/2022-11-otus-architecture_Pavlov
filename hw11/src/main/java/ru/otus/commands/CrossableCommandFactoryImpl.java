package ru.otus.commands;

import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class CrossableCommandFactoryImpl implements CrossableCommandFactory {

    private final BiFunction<Crossable, Crossable, Command> crossableCommandFactory;

    public CrossableCommandFactoryImpl(BiFunction<Crossable, Crossable, Command> crossableCommandFactory) {
        this.crossableCommandFactory = crossableCommandFactory;
    }

    @Override
    public List<Command> apply(Crossable crossable, Surrounding surrounding) {
        return Optional.ofNullable(surrounding)
                .map(Surrounding::getAll)
                .orElseGet(ArrayList::new)
                .stream()
                .filter(Objects::nonNull)
                .filter(value -> !value.equals(crossable))
                .map(value -> crossableCommandFactory.apply(crossable, value))
                .toList();
    }
}
