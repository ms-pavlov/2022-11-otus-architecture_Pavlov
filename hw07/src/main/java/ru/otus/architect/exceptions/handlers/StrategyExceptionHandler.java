package ru.otus.architect.exceptions.handlers;

import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.LogCommand;
import ru.otus.architect.commands.RetryCommand;
import ru.otus.architect.exceptions.ExceptionHandler;
import ru.otus.architect.exceptions.RetriesStrategy;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class StrategyExceptionHandler implements ExceptionHandler {

    private final Queue<Command> commandQueue;
    private final RetriesStrategy retriesStrategy;
    private final Map<Command, AtomicInteger> retries;
    private final Logger logger;

    public StrategyExceptionHandler(Queue<Command> commandQueue, RetriesStrategy retriesStrategy,
                                    Map<Command, AtomicInteger> retries, Logger logger) {
        this.commandQueue = commandQueue;
        this.retriesStrategy = retriesStrategy;
        this.retries = retries;
        this.logger = logger;
    }

    @Override
    public void handle(Command cmd, Exception ex) {
        var allowedRetries = retriesStrategy.getAllowedRetries(cmd.getClass());
        var madeRetries = retries.getOrDefault(cmd, new AtomicInteger(0));
        if (madeRetries.incrementAndGet() < allowedRetries) {
            retries.put(cmd, madeRetries);
            commandQueue.offer(new RetryCommand(cmd, commandQueue));
        } else {
            commandQueue.offer(new LogCommand(cmd, ex, logger));
        }
    }
}
