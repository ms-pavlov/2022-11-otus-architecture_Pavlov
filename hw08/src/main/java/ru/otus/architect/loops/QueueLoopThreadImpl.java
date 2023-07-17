package ru.otus.architect.loops;

import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.ExceptionHandlingStrategy;

import java.util.Optional;
import java.util.function.Supplier;

public class QueueLoopThreadImpl implements QueueLoopThread {
    private final Supplier<Command> commandSupplierStrategy;
    private final ExceptionHandlingStrategy handlingStrategy;
    private CommandHandler commandHandler;
    private StopHandler stopHandler;

    public QueueLoopThreadImpl(
            Supplier<Command> commandSupplierStrategy,
            ExceptionHandlingStrategy handlingStrategy,
            CommandHandler commandHandler,
            StopHandler stopHandler) {
        this.commandSupplierStrategy = commandSupplierStrategy;
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
                Command command = this.commandSupplierStrategy.get();

                try {
                    Optional.ofNullable(command)
                            .ifPresent(commandHandler);
                } catch (Exception exception) {
                    Optional.ofNullable(this.handlingStrategy)
                            .map((strategies) -> strategies.getHandler(Command.class, exception.getClass()))
                            .orElseThrow(RuntimeException::new)
                            .handle(command, exception);
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
