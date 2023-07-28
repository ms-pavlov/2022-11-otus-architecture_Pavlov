package ru.otus.objects;

import ru.otus.mesh.PlayingField;
import ru.otus.mesh.Surrounding;

import java.util.List;

public interface Crossable {

    boolean hasCrossing(Crossable crossable);

    List<Surrounding> getSurroundings();

    void setSurrounding(PlayingField field, Surrounding surrounding);

    void removeSurrounding(PlayingField field);

    Surrounding getSurrounding(PlayingField field);
}
