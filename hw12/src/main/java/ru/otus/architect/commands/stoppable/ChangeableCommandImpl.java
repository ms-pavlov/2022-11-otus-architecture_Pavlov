package ru.otus.architect.commands.stoppable;

import ru.otus.architect.commands.Command;

/*
 * Подменяет Одну команду другой.
 * Используется для остановки команды.
 */
public class ChangeableCommandImpl implements ChangeableCommand {

    private Command command;

    public ChangeableCommandImpl(Command command) {
        this.command = command;
    }

    @Override
    public void changeCommand(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        command.execute();
    }
}
