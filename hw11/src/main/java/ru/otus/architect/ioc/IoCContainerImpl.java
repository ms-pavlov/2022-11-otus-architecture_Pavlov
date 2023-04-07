package ru.otus.architect.ioc;

import ru.otus.architect.ioc.storages.IoCStorage;

public class IoCContainerImpl implements IoCContainer {

    private final IoCStorage iocStorage;

    public IoCContainerImpl(IoCStorage iocStorage) {
        this.iocStorage = iocStorage;
    }

    @Override
    public <T> T resolve(String name, Object... args) {
        return (T) iocStorage.get(name).create(args);
    }

}
