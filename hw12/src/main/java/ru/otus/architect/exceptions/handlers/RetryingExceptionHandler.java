package ru.otus.architect.exceptions.handlers;

import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.RetryCommand;
import ru.otus.architect.exceptions.ExceptionHandler;

import java.util.Queue;

public class RetryingExceptionHandler implements ExceptionHandler {

    private final Queue<Command> commandQueue;

    public RetryingExceptionHandler(Queue<Command> commandQueue) {
        this.commandQueue = commandQueue;
    }

    @Override
    public void handle(Command cmd, Exception ex) {
        commandQueue.offer(new RetryCommand(cmd, commandQueue));
    }
}
