package ru.otus.architect.adapter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.architect.processor.DependencyNameGeneratorFactory;

import java.lang.reflect.InvocationHandler;
import java.util.function.BiFunction;

@Component
public class AdapterInvocationHandlerProviderImpl implements InvocationHandlerProvider {
    private final BiFunction<String, Object[], Object> dependencyIoCStrategy;
    private final DependencyNameGeneratorFactory generatorFactory;

    public AdapterInvocationHandlerProviderImpl(
            @Qualifier("dependencyIoCStrategy") BiFunction<String, Object[], Object> dependencyIoCStrategy,
            @Qualifier("dependencyNameGeneratorFactory") DependencyNameGeneratorFactory generatorFactory) {
        this.dependencyIoCStrategy = dependencyIoCStrategy;
        this.generatorFactory = generatorFactory;
    }

    @Override
    public InvocationHandler getHandler(Object object) {
        return new AdapterInvocationHandler(object, dependencyIoCStrategy, generatorFactory);
    }
}
