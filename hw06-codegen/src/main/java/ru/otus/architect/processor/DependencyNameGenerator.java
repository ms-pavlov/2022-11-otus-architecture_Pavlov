package ru.otus.architect.processor;

public interface DependencyNameGenerator {

    String getPluginClassName();

    String getDependencyName(String method);
}
