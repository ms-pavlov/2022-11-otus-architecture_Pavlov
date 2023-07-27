package ru.otus.architect.adapter;

public interface AdapterGenerator {

    Object generate(Class<?> interfaceClass, Object object);
}
