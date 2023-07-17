package ru.otus.architect.adapter;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.processor.DependencyNameGeneratorFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class AdapterInvocationHandler implements InvocationHandler {

    private final Object object;
    private final BiFunction<String, Object[], Object> dependencyIoCStrategy;
    private final DependencyNameGeneratorFactory generatorFactory;

    public AdapterInvocationHandler(
            Object object,
            @Qualifier("dependencyIoCStrategy") BiFunction<String, Object[], Object> dependencyIoCStrategy,
            DependencyNameGeneratorFactory generatorFactory) {
        this.object = object;
        this.dependencyIoCStrategy = dependencyIoCStrategy;
        this.generatorFactory = generatorFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        var nameGenerator = generatorFactory.create(
                object.getClass().getSimpleName(),
                method.getDeclaringClass().getSimpleName());
        return dependencyIoCStrategy.apply(
                nameGenerator.getDependencyName(method.getName()),
                prepareParams(args));

    }

    private Object[] prepareParams(Object[] args) {
        var params = Optional.ofNullable(args)
                .map(List::of)
                .map(LinkedList::new)
                .orElseGet(LinkedList::new);
        params.addFirst(object);
        return params.toArray();
    }

}
