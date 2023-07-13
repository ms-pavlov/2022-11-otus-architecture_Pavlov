package ru.otus.architect.ioc.plugins;

import ru.otus.architect.ioc.IoCContainer;

@FunctionalInterface
public interface IoCPlugin {

    void execute();
}
