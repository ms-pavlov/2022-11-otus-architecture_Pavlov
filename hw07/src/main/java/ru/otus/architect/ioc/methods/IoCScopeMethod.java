package ru.otus.architect.ioc.methods;

import ru.otus.architect.ioc.storages.IoCStorage;

public class IoCScopeMethod implements FactoryMethod {

    private final IoCStorage iocStorage;

    public IoCScopeMethod(IoCStorage iocStorage) {
        this.iocStorage = iocStorage;
    }

    @Override
    public Object create(Object... args) {
        if (1 != args.length || !(args[0] instanceof String)) {
            throw new RuntimeException();
        }
        iocStorage.setScope((String) args[0]);
        return null;
    }
}
