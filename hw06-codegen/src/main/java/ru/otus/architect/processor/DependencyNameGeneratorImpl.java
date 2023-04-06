package ru.otus.architect.processor;

import lombok.Builder;
import ru.otus.architect.ioc.IoCContainer;

import javax.lang.model.element.Element;

@Builder
public class DependencyNameGeneratorImpl implements DependencyNameGenerator{
    private final static String CLASS_POSTFIX = "AdapterPlugin";
    private final static String METHOD_POSTFIX = "IoC.";
    private final static String CLASS_SEPARATOR = ".";
    private final static String METHOD_SEPARATOR = "::";

    private final String sourceClassName;
    private final String interfaceClassName;

    public DependencyNameGeneratorImpl(String sourceClassName, String interfaceClassName) {
        this.sourceClassName = sourceClassName;
        this.interfaceClassName = interfaceClassName;
    }

    @Override
    public String getPluginClassName() {
        return sourceClassName + interfaceClassName + CLASS_POSTFIX;
    }

    @Override
    public String getDependencyName(String method) {
        return METHOD_POSTFIX
                .concat(sourceClassName)
                .concat(CLASS_SEPARATOR)
                .concat(interfaceClassName)
                .concat(METHOD_SEPARATOR)
                .concat(method);
    }
}
