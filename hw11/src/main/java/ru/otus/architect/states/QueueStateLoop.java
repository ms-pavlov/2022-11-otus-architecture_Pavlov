package ru.otus.architect.states;

import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.ExceptionHandlingStrategy;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class QueueStateLoop implements QueueLoop{
    private final BlockingQueue<Command> commands;
    private final ExceptionHandlingStrategy handlingStrategy;
    private LoopState state;


    public QueueStateLoop(BlockingQueue<Command> commands, ExceptionHandlingStrategy handlingStrategy, LoopState state) {
        this.commands = commands;
        this.handlingStrategy = handlingStrategy;
        this.state = state;
    }

    @Override
    public void run() {
        while(null != state) {
            try {
                Command command = this.commands.take();
                try {
                    state.accept(command);
                    state = state.next();
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

    @Override
    public LoopState getState() {
        return state;
    }
}
