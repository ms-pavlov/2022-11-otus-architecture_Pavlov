package ru.otus.mesh;

import ru.otus.objects.Crossable;

import java.util.List;

public interface Surrounding {

    boolean contains(Crossable crossable);

    List<Crossable> getAll();

    void add(Crossable object);

    void remove(Crossable object);
}
