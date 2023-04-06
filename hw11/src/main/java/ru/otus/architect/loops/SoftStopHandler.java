package ru.otus.architect.loops;

import java.util.concurrent.BlockingQueue;
import ru.otus.architect.commands.Command;

public class SoftStopHandler implements StopHandler {
    private final BlockingQueue<Command> commands;

    public SoftStopHandler(BlockingQueue<Command> commands) {
        this.commands = commands;
    }

    public Boolean get() {
        return !this.commands.isEmpty();
    }
}

