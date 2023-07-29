package ru.otus.commands.strategies;

import ru.otus.mesh.Surrounding;
import ru.otus.objects.Crossable;

import java.util.function.BiConsumer;

public interface ChangeSurroundingStrategy extends BiConsumer<Crossable, Surrounding> {
}
