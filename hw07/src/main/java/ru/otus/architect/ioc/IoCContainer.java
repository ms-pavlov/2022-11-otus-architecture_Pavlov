package ru.otus.architect.ioc;

public interface IoCContainer {

    <T> T resolve(String name, Object... args);
}
