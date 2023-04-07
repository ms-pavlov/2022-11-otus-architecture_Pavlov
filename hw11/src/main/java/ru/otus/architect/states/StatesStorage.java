package ru.otus.architect.states;

public interface StatesStorage  {

    LoopState getState(String name);
}
