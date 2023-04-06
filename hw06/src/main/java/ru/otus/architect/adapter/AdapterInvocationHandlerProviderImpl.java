package ru.otus.architect.adapter;

import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.processor.DependencyNameGeneratorFactory;

import java.lang.reflect.InvocationHandler;

public class AdapterInvocationHandlerProviderImpl implements InvocationHandlerProvider{
    private final IoCContainer container;
    private final DependencyNameGeneratorFactory generatorFactory;

    public AdapterInvocationHandlerProviderImpl(IoCContainer container, DependencyNameGeneratorFactory generatorFactory) {
        this.container = container;
        this.generatorFactory = generatorFactory;
    }

    @Override
    public InvocationHandler getHandler(Object object) {
        return new AdapterInvocationHandler(object, container, generatorFactory);
    }
}
