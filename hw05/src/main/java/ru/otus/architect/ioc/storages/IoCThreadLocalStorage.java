package ru.otus.architect.ioc.storages;

import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.methods.IoCRegisterMethod;
import ru.otus.architect.ioc.methods.IoCScopeMethod;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class IoCThreadLocalStorage implements IoCStorage {
    public final static String DEFAULT_SCOPE = "main";

    private final Map<String, Map<String, FactoryMethod>> storage;
    private final IoCStoragePlugin scopeInit;
    private final ThreadLocal<String> scope;

    public IoCThreadLocalStorage(Map<String, Function<IoCStorage, FactoryMethod>> initMethods) {
        this.storage = new ConcurrentHashMap<>();
        this.scope = ThreadLocal.withInitial(() -> DEFAULT_SCOPE);
        this.storage.put(DEFAULT_SCOPE, new ConcurrentHashMap<>());
        this.scopeInit = new IoCStorageGroupPlugin(
                initMethods.entrySet()
                        .stream()
                        .map(entry -> (IoCStoragePlugin) store -> store.put(
                                entry.getKey(), entry.getValue().apply(store)
                        ))
                        .toList());
        scopeInit.execute(this);

    }

    @Override
    public void setScope(String scope) {
        this.scope.set(scope);
        if (!storage.containsKey(scope)) {
            storage.put(scope, new ConcurrentHashMap<>());
            scopeInit.execute(this);
        }
    }

    @Override
    public void setDefaultScope() {
        this.scope.set(DEFAULT_SCOPE);
    }

    @Override
    public FactoryMethod get(String name) {
        var map = storage.get(scope.get());
        var scopeName = scope.get();
        return Optional.ofNullable(storage.get(scope.get()))
                .map(value -> value.get(name))
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void put(String name, FactoryMethod method) {
        Optional.ofNullable(storage)
                .map(value -> value.get(scope.get()))
                .map(value -> {
                    value.put(name, method);
                    return value;
                })
                .orElseThrow(RuntimeException::new);
    }
}
