package ru.otus.architect.commands;

import java.util.Queue;

public class RetryCommand implements Command {
    private final Command command;
    private final Queue<Command> commandQueue;

    public RetryCommand(Command command, Queue<Command> commandQueue) {
        this.command = command;
        this.commandQueue = commandQueue;
    }

    @Override
    public void execute() {
        commandQueue.offer(command);
    }
}
