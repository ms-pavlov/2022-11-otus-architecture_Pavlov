package ru.otus.architect.adapter;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.stereotype.Component;

@Component
public class AdapterFactoryImpl implements AdapterFactory {
    private final Table<Class<?>, Object, Object> adapters;
    private final AdapterGenerator generator;

    public AdapterFactoryImpl(AdapterGenerator generator) {
        this.adapters = HashBasedTable.create();
        this.generator = generator;
    }

    @Override
    public Object getAdapter(Class<?> interfaceClass, Object object) {
        if (!adapters.contains(interfaceClass, object)) {
            return createAdapter(interfaceClass, object);
        }
        return adapters.get(interfaceClass, object);
    }

    private Object createAdapter(Class<?> interfaceClass, Object object) {
        var adapter = generator.generate(interfaceClass, object);
        adapters.put(interfaceClass, object, adapter);
        return adapter;
    }
}
