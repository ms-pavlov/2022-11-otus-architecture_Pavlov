package ru.otus.architect.exceptions.strategy;

import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.RetriesStrategy;

import java.util.Map;

public class RetriesStrategyRepository implements RetriesStrategy {

    private final Map<Class<? extends Command>, Integer> allowedRetries;

    public RetriesStrategyRepository(Map<Class<? extends Command>, Integer> allowedRetries) {
        this.allowedRetries = allowedRetries;
    }

    @Override
    public void registerStrategy(Class<? extends Command> commandClass, int retries) {
        allowedRetries.put(commandClass, retries);
    }

    @Override
    public int getAllowedRetries(Class<? extends Command> commandClass) {
        return allowedRetries.getOrDefault(commandClass, 1);
    }
}
