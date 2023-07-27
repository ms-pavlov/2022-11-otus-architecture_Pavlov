package ru.otus.architect.ioc.methods;

import ru.otus.architect.ioc.storages.IoCStorage;

public class IoCRegisterMethod implements FactoryMethod {
    private final IoCStorage iocStorage;

    public IoCRegisterMethod(IoCStorage iocStorage) {
        this.iocStorage = iocStorage;
    }

    @Override
    public Object create(Object... args) {
        if (2 != args.length || !(args[0] instanceof String) || !(args[1] instanceof FactoryMethod)) {
            throw new RuntimeException();
        }
        iocStorage.put((String) args[0], (FactoryMethod) args[1]);
        return null;
    }
}
