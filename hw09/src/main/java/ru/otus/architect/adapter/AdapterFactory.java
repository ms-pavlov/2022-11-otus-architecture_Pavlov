package ru.otus.architect.adapter;

public interface AdapterFactory {

    Object getAdapter(Class<?> interfaceClass, Object object);
}
