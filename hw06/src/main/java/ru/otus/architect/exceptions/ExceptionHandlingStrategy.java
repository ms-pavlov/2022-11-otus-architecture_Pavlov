package ru.otus.architect.exceptions;

import ru.otus.architect.commands.Command;

public interface ExceptionHandlingStrategy {
    void registerStrategy(Class<? extends Command> commandClass,
                          Class<? extends Exception> exceptionClass,
                          ExceptionHandler exceptionHandler);

    void registerStrategyOnCommand(Class<? extends Command> commandClass,
                                   ExceptionHandler exceptionHandler);

    void registerStrategyOnException(Class<? extends Exception> exceptionClass,
                                     ExceptionHandler exceptionHandler);

    ExceptionHandler getHandler(Class<? extends Command> commandClass, Class<? extends Exception> exceptionClass);
}
