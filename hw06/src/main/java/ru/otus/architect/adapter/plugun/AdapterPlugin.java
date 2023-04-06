package ru.otus.architect.adapter.plugun;

import ru.otus.architect.adapter.AdapterFactory;
import ru.otus.architect.adapter.AdapterFactoryImpl;
import ru.otus.architect.adapter.AdapterGeneratorImpl;
import ru.otus.architect.adapter.AdapterInvocationHandlerProviderImpl;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.plugun.IoCPlugin;
import ru.otus.architect.processor.DependencyNameGeneratorImpl;

public class AdapterPlugin implements IoCPlugin {

    private final static String DEPENDENCY_NAME = "Adapter";

    private final String registrationName;
    private final String scopeNewName;
    private final String scopeName;
    private final IoCContainer container;
    private final AdapterFactory factory;


    public AdapterPlugin(
            String registrationName,
            String scopeNewName,
            String scopeName,
            IoCContainer container) {
        this.registrationName = registrationName;
        this.scopeNewName = scopeNewName;
        this.container = container;
        this.scopeName = scopeName;
        this.factory = new AdapterFactoryImpl(
                new AdapterGeneratorImpl(
                        new AdapterInvocationHandlerProviderImpl(
                                container,
                                DependencyNameGeneratorImpl::new)));
    }

    @Override
    public void execute() {
        container.resolve(scopeNewName, scopeName);
        register(DEPENDENCY_NAME, args -> {
            if (2 != args.length) {
                throw  new RuntimeException();
            }
            return factory.getAdapter((Class<?>) args[0], args[1]);
        });
    }

    private void register(String name, FactoryMethod method) {
        container.resolve(registrationName, name, method);
    }
}
