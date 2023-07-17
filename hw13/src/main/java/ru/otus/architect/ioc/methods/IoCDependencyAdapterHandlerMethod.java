package ru.otus.architect.ioc.methods;

import ru.otus.architect.ioc.storages.IoCStorage;
import ru.otus.architect.processor.DependencyNameGenerator;
import ru.otus.architect.processor.DependencyNameGeneratorFactory;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class IoCDependencyAdapterHandlerMethod implements FactoryMethod {

    private final DependencyNameGeneratorFactory generatorFactory;
    private final IoCStorage iocStorage;

    public IoCDependencyAdapterHandlerMethod(
            IoCStorage iocStorage,
            DependencyNameGeneratorFactory generatorFactory) {
        this.generatorFactory = generatorFactory;
        this.iocStorage = iocStorage;
    }


    @Override
    public Object create(Object... args) {
        if(1 != args.length && !(args[0] instanceof Class)) {
            throw new RuntimeException();
        }
        Class<?> clazz = (Class<?>) args[0];
        return Stream.of(clazz.getMethods())
                .map(Method::getName)
                .allMatch(
                        methodName -> iocStorage.contains(
                                generatorFactory.create("\\S*.", clazz.getSimpleName()).getDependencyName(methodName)));
    }
}
