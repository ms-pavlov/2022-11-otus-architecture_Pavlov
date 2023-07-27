package ru.otus.architect.ioc;

public interface IoCContainer {

    Object resolve(String name, Object... args);
}
