package ru.otus.architect.loops;

import ru.otus.architect.commands.Command;

import java.util.concurrent.BlockingQueue;

public class SoftStopCommand implements Command {
    private final QueueLoopThread loopThread;
    private final BlockingQueue<Command> commands;

    public SoftStopCommand(
            QueueLoopThread loopThread,
            BlockingQueue<Command> commands) {
        this.loopThread = loopThread;
        this.commands = commands;
    }

    @Override
    public void execute() {
        loopThread.setStopHandler(new SoftStopHandler(commands));
    }
}
