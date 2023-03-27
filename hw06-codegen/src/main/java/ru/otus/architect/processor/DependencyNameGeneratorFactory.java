package ru.otus.architect.processor;

public interface DependencyNameGeneratorFactory {
    DependencyNameGenerator create(String sourceClass, String interfaceClass);
}
