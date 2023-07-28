package ru.otus.commands.stoppable;

import ru.otus.commands.Command;

public interface ChangeableCommand extends Command {

    void changeCommand(Command command);
}
