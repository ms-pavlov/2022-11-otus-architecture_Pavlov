package ru.otus.architect.states;

import ru.otus.architect.commands.Command;

import java.util.concurrent.BlockingQueue;

public class DefaultLoopState implements LoopState{

    private LoopState state;

    public DefaultLoopState() {
        this.state = this;
    }

    @Override
    public void moveTo(BlockingQueue<Command> queue) {
        this.state = new RedirectLoopState(queue);
    }

    @Override
    public void stop() {
        this.state = null;
    }

    @Override
    public void accept(Command command) {
        command.execute();
    }

    @Override
    public LoopState next() {
        return state;
    }
}
