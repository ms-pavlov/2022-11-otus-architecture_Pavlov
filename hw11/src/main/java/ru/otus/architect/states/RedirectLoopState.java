package ru.otus.architect.states;

import ru.otus.architect.commands.Command;

import java.util.concurrent.BlockingQueue;

public class RedirectLoopState implements LoopState {
    private LoopState state;
    private final BlockingQueue<Command> queue;

    public RedirectLoopState(BlockingQueue<Command> queue) {
        this.state = this;
        this.queue = queue;
    }

    @Override
    public void doDefault() {
        this.state = new DefaultLoopState();
    }

    @Override
    public void stop() {
        this.state = null;
    }

    @Override
    public LoopState next() {
        return state;
    }

    @Override
    public void accept(Command command) {
        try {
            queue.put(command);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
