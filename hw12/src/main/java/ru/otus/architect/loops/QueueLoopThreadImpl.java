package ru.otus.architect.loops;

import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.ExceptionHandlingStrategy;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class QueueLoopThreadImpl implements QueueLoopThread {
    private final BlockingQueue<Command> commands;
    private final ExceptionHandlingStrategy handlingStrategy;
    private CommandHandler commandHandler;
    private StopHandler stopHandler;

    public QueueLoopThreadImpl(BlockingQueue<Command> commands, ExceptionHandlingStrategy handlingStrategy, CommandHandler commandHandler, StopHandler stopHandler) {
        this.commands = commands;
        this.handlingStrategy = handlingStrategy;
        this.commandHandler = commandHandler;
        this.stopHandler = stopHandler;
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void setStopHandler(StopHandler stopHandler) {
        this.stopHandler = stopHandler;
    }

    public void run() {
        while(this.stopHandler.get()) {
            try {
                Command command = this.commands.take();

                try {
                    this.commandHandler.accept(command);
                } catch (Exception exception) {
                    Optional.ofNullable(this.handlingStrategy)
                            .map((strategies) -> strategies.getHandler(Command.class, exception.getClass()))
                            .orElseThrow(RuntimeException::new)
                            .handle(null, exception);
                }
            } catch (Exception exception) {
                Optional.ofNullable(this.handlingStrategy)
                        .map((strategies) -> strategies.getHandler(Command.class, exception.getClass()))
                        .orElseThrow(RuntimeException::new)
                        .handle(null, exception);
            }
        }

    }
}
