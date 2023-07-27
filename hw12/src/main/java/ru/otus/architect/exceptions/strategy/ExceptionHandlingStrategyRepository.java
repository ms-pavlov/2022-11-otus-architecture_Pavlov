package ru.otus.architect.exceptions.strategy;

import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.ExceptionHandler;
import ru.otus.architect.exceptions.ExceptionHandlingStrategy;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ExceptionHandlingStrategyRepository implements ExceptionHandlingStrategy {
    private final Map<Class<? extends Command>, Map<Class<? extends Exception>, ExceptionHandler>> handlers;
    private final ExceptionHandler defaultExceptionHandler;

    public ExceptionHandlingStrategyRepository(Map<Class<? extends Command>, Map<Class<? extends Exception>,
            ExceptionHandler>> handlers, ExceptionHandler defaultExceptionHandler) {
        this.handlers = handlers;
        this.defaultExceptionHandler = defaultExceptionHandler;
    }

    @Override
    public void registerStrategy(Class<? extends Command> commandClass, Class<? extends Exception> exceptionClass, ExceptionHandler exceptionHandler) {
        handlers.put(commandClass, Map.of(exceptionClass, exceptionHandler));
    }

    @Override
    public void registerStrategyOnCommand(Class<? extends Command> commandClass, ExceptionHandler exceptionHandler) {
        handlers.put(commandClass, Map.of(Exception.class, exceptionHandler));
    }

    @Override
    public void registerStrategyOnException(Class<? extends Exception> exceptionClass, ExceptionHandler exceptionHandler) {
        handlers.put(Command.class, Map.of(exceptionClass, exceptionHandler));
    }

    @Override
    public ExceptionHandler getHandler(Class<? extends Command> commandClass, Class<? extends Exception> exceptionClass) {
        var byCommand = Optional.ofNullable(handlers.getOrDefault(commandClass,
                handlers.get(Command.class))).orElse(Collections.emptyMap());
        return byCommand.getOrDefault(exceptionClass, byCommand.getOrDefault(Exception.class, defaultExceptionHandler));
    }
}
