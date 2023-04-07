package ru.otus.architect.exceptions.handlers;

import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.LogCommand;
import ru.otus.architect.exceptions.ExceptionHandler;

import java.util.logging.Logger;

public class LoggingExceptionHandler implements ExceptionHandler {

    private final Logger logger;

    public LoggingExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(Command cmd, Exception ex) {
        var command = new LogCommand(cmd, ex, logger);
        command.execute();
    }
}
