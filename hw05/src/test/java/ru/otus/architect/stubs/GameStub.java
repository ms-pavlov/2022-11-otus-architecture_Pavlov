package ru.otus.architect.stubs;

import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.ExceptionHandlingStrategy;

import java.util.Queue;

public class GameStub {

    private final Queue<Command> commandQueue;
    private final ExceptionHandlingStrategy exceptionHandlingStrategy;

    public GameStub(Queue<Command> commandQueue, ExceptionHandlingStrategy exceptionHandlingStrategy) {
        this.commandQueue = commandQueue;
        this.exceptionHandlingStrategy = exceptionHandlingStrategy;
    }

    public void run() {

        while (!commandQueue.isEmpty()) {
            var cmd = commandQueue.poll();

            try {
                cmd.execute();
            } catch (Exception ex) {
                var handler = exceptionHandlingStrategy.getHandler(cmd.getClass(), ex.getClass());
                handler.handle(cmd, ex);
            }
        }
    }
}
