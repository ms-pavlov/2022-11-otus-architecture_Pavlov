package ru.otus.architect.ioc.methods;

import java.util.stream.Stream;

public class IoCommandInterfacesListMethod implements FactoryMethod{

    @Override
    public Object create(Object... args) {
        if(1 != args.length && !(args[0] instanceof String)) {
            throw new RuntimeException();
        }
        try {
            return Stream.of(Class.forName((String) args[0]).getConstructors())
                    .flatMap(constructor -> Stream.of(constructor.getParameterTypes()))
                    .toList();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
