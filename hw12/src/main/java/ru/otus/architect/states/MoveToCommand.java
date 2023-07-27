package ru.otus.architect.states;

import ru.otus.architect.commands.Command;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class MoveToCommand implements Command {

    private final QueueLoop queueLoop;
    private final BlockingQueue<Command> commands;

    public MoveToCommand(QueueLoop queueLoop, BlockingQueue<Command> commands) {
        this.queueLoop = queueLoop;
        this.commands = commands;
    }

    @Override
    public void execute() {
        Optional.ofNullable(queueLoop)
                .map(QueueLoop::getState)
                .ifPresent(state -> state.moveTo(commands));
    }
}
