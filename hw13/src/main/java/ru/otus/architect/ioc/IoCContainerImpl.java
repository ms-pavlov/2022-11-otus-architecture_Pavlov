package ru.otus.architect.ioc;

import org.springframework.stereotype.Component;
import ru.otus.architect.ioc.storages.IoCStorage;

@Component
public class IoCContainerImpl implements IoCContainer {

    private final IoCStorage iocStorage;

    public IoCContainerImpl(IoCStorage iocStorage) {
        this.iocStorage = iocStorage;
    }

    @Override
    public Object resolve(String name, Object... args) {
        return iocStorage.get(name).create(args);
    }

}
