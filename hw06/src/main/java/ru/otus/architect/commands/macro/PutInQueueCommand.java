package ru.otus.architect.commands.macro;

import ru.otus.architect.commands.Command;

import java.util.Queue;

public class PutInQueueCommand implements Command {
    private final Command command;
    private final Queue<Command> commandQueue;

    public PutInQueueCommand(Command command, Queue<Command> commandQueue) {
        this.command = command;
        this.commandQueue = commandQueue;
    }

    @Override
    public void execute() {
        commandQueue.offer(command);
    }
}
