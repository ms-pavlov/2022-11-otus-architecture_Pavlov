package ru.otus.architect.commands;

import java.util.Optional;
import java.util.Queue;

public class QueueOfferCommand implements Command{

    private final Queue<Command> queue;
    private final Command command;

    public QueueOfferCommand(Queue<Command> queue, Command command) {
        this.queue = queue;
        this.command = command;
    }

    @Override
    public void execute() {
        Optional.ofNullable(queue)
                .filter(queue -> queue.offer(command))
                .orElseThrow(() -> new RuntimeException("Can't offer command to queue"));
    }
}
