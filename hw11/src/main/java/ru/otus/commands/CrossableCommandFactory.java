package ru.otus.commands;

import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.List;
import java.util.function.BiFunction;

public interface CrossableCommandFactory extends BiFunction<Crossable, Surrounding, List<Command>> {
}
