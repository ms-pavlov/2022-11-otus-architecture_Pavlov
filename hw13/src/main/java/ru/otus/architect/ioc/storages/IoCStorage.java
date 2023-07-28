package ru.otus.architect.ioc.storages;

import ru.otus.architect.ioc.methods.FactoryMethod;


public interface IoCStorage {

    void setScope(String scope);

    void setDefaultScope();

    FactoryMethod get(String name);

    void put(String name, FactoryMethod method);

    boolean contains(String pattern);
}
