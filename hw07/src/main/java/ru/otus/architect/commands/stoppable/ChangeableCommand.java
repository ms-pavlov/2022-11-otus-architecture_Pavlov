package ru.otus.architect.commands.stoppable;

import ru.otus.architect.commands.Command;

public interface ChangeableCommand extends Command {

    void changeCommand(Command command);
}
