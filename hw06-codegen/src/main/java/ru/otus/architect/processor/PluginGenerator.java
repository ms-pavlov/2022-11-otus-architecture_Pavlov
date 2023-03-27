package ru.otus.architect.processor;

import com.squareup.javapoet.TypeSpec;

public interface PluginGenerator {

    TypeSpec prepPlugin();
}
