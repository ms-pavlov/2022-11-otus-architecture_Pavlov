package ru.otus.architect.loops;

import java.util.concurrent.ExecutorService;
import ru.otus.architect.commands.Command;

public class QueueLoopThreadCommand implements Command {
    private final QueueLoopThread loopThread;
    private final ExecutorService executorService;

    public QueueLoopThreadCommand(QueueLoopThread loopThread, ExecutorService executorService) {
        this.loopThread = loopThread;
        this.executorService = executorService;
    }

    public void execute() {
        this.executorService.execute(this.loopThread);
    }
}