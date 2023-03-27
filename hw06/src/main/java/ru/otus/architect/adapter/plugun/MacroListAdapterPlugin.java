package ru.otus.architect.adapter.plugun;

import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.plugun.IoCPlugin;

import java.util.List;

public class MacroListAdapterPlugin implements IoCPlugin {
    private final static String CONTAINS_DEPENDENCY_NAME = "IoC.Contains";
    private final static String CHECK_DEPENDENCY_NAME = "IoC.Check.Command";
    private final String registrationName;
    private final String scopeNewName;
    private final String scopeName;
    private final IoCContainer container;

    public MacroListAdapterPlugin(
            String registrationName,
            String scopeNewName,
            String scopeName,
            IoCContainer container) {
        this.registrationName = registrationName;
        this.scopeNewName = scopeNewName;
        this.scopeName = scopeName;
        this.container = container;
    }

    @Override
    public void execute() {
        container.resolve(scopeNewName, scopeName);
        register(CONTAINS_DEPENDENCY_NAME, args -> {
            if (1 != args.length) {
                throw  new RuntimeException();
            }
            return null;
        });
    }

    private void register(String name, FactoryMethod method) {
        container.resolve(registrationName, name, method);
    }
}
