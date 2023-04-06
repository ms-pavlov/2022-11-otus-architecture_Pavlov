package ru.otus.architect.loops;

import ru.otus.architect.commands.Command;

public class HardStopCommand implements Command {

    private final QueueLoopThread loopThread;

    public HardStopCommand(QueueLoopThread loopThread) {
        this.loopThread = loopThread;
    }

    @Override
    public void execute() {
        loopThread.setStopHandler(new HardStopHandler());
    }
}
