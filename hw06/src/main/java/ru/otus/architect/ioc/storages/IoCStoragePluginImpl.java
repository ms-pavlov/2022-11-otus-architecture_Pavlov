package ru.otus.architect.ioc.storages;

import ru.otus.architect.ioc.methods.FactoryMethod;

public class IoCStoragePluginImpl implements IoCStoragePlugin{
    private final String name;
    private final FactoryMethod method;

    public IoCStoragePluginImpl(String name, FactoryMethod method) {
        this.name = name;
        this.method = method;
    }

    @Override
    public void execute(IoCStorage storage) {
        storage.put(name, method);
    }
}
