package ru.otus.architect.adapter.plugun;

import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.plugun.IoCPlugin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MacroListAdapterPlugin implements IoCPlugin {

    private final static String DEPENDENCY_COMMAND = "IoC.Adapter::checkCommandInterface";

    private final String registrationName;
    private final String scopeNewName;
    private final String scopeName;
    private final String dependencyInterfaceName;
    private final String dependencyAdapterHandler;
    private final IoCContainer container;

    public MacroListAdapterPlugin(
            String registrationName,
            String scopeNewName,
            String scopeName,
            String dependencyInterfaceName,
            String dependencyAdapterHandler,
            IoCContainer container) {
        this.registrationName = registrationName;
        this.scopeNewName = scopeNewName;
        this.scopeName = scopeName;
        this.dependencyInterfaceName = dependencyInterfaceName;
        this.dependencyAdapterHandler = dependencyAdapterHandler;
        this.container = container;
    }

    @Override
    public void execute() {
        container.resolve(scopeNewName, scopeName);
        container.resolve(
                registrationName,
                DEPENDENCY_COMMAND,
                (FactoryMethod) args -> Stream.of(args)
                        .collect(Collectors.toMap(
                                name -> name,
                                name -> container.<List<Class<?>>>resolve(
                                                dependencyInterfaceName,
                                                name)
                                        .stream()
                                        .allMatch(clazz -> container.<Boolean>resolve(
                                                dependencyAdapterHandler,
                                                clazz)))));
    }
}
