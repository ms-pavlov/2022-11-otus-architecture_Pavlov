package ru.otus.architect.exceptions.handlers;

import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.CommandException;
import ru.otus.architect.exceptions.ExceptionHandler;

import java.util.Queue;

public class StepByStepCommandExceptionHandler implements ExceptionHandler {

    private final Queue<Command> commands;

    public StepByStepCommandExceptionHandler(Queue<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void handle(Command cmd, Exception ex) {
        throw new CommandException(ex.getMessage(), ex);
    }
}
