package ru.otus.architect.commands;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class LogCommand implements Command {
    private final Logger logger;
    private final Supplier<String> message;

    public LogCommand(Supplier<String> message, Logger logger) {
        this.logger = logger;
        this.message = message;
    }

    @Override
    public void execute() {
        logger.log(logger.getLevel(), message);
    }
}
