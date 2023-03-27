package ru.otus.architect.adapter;

import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.processor.DependencyNameGeneratorFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AdapterInvocationHandler implements InvocationHandler {

    private final Object object;
    private final IoCContainer container;
    private final DependencyNameGeneratorFactory generatorFactory;

    public AdapterInvocationHandler(Object object, IoCContainer container, DependencyNameGeneratorFactory generatorFactory) {
        this.object = object;
        this.container = container;
        this.generatorFactory = generatorFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        var nameGenerator = generatorFactory.create(
                object.getClass().getSimpleName(),
                method.getDeclaringClass().getSimpleName());
        return container.resolve(
                nameGenerator.getDependencyName(method.getName()),
                object);
    }

}
