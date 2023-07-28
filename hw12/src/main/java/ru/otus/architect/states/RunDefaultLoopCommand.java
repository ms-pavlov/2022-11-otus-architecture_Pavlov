package ru.otus.architect.states;

import ru.otus.architect.commands.Command;

import java.util.Optional;

public class RunDefaultLoopCommand implements Command {

    private final QueueLoop queueLoop;

    public RunDefaultLoopCommand(QueueLoop queueLoop) {
        this.queueLoop = queueLoop;
    }

    @Override
    public void execute() {
        Optional.ofNullable(queueLoop)
                .map(QueueLoop::getState)
                .ifPresent(LoopState::doDefault);
    }
}
