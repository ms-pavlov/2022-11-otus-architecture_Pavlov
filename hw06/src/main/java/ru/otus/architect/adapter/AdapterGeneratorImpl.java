package ru.otus.architect.adapter;

import java.lang.reflect.Proxy;

public class AdapterGeneratorImpl implements AdapterGenerator{

    private final InvocationHandlerProvider handlerProvider;

    public AdapterGeneratorImpl(InvocationHandlerProvider handlerProvider) {
        this.handlerProvider = handlerProvider;
    }

    @Override
    public Object generate(Class<?> interfaceClass, Object object) {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{interfaceClass}, handlerProvider.getHandler(object));
    }
}
