package ru.otus.architect.adapter;

import java.lang.reflect.InvocationHandler;

public interface InvocationHandlerProvider {

    InvocationHandler getHandler(Object object);
}
