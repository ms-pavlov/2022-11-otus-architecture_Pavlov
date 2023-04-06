package ru.otus.architect.exceptions;


import ru.otus.architect.commands.Command;

public interface RetriesStrategy {
    void registerStrategy(Class<? extends Command> commandClass, int allowedRetries);
    int getAllowedRetries(Class<? extends Command> commandClass);
}
