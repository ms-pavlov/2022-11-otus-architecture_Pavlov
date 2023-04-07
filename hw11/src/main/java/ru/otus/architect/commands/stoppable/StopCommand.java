package ru.otus.architect.commands.stoppable;

import ru.otus.architect.commands.Command;

public class StopCommand implements Command {
    private final Stoppable command;

    public StopCommand(StoppableCommand command) {
        this.command = command;
    }

    @Override
    public void execute() {
        command.stop();
    }
}
