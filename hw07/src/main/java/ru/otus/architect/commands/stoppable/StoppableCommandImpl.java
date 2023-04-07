package ru.otus.architect.commands.stoppable;

import ru.otus.architect.commands.Command;

/*
 * Команда которую можно остановить.
 * При вызове stop() действия в текущей команде заменяется на действия необходимые для кавершения завершения.
 * Когда после выполнения stop() очередь дойдет до команды будут выполнены действия необходимые для завершения команды
 * и выполнение команды прекратиться.
 *
 * Например если terminationCommand = () -> return, ни каких действий не произайдет и команда завершиться.
 */
public class StoppableCommandImpl implements StoppableCommand {

    private final ChangeableCommand command;
    private final Command terminationCommand;

    public StoppableCommandImpl(Command command, Command terminationCommand) {
        this.command = new ChangeableCommandImpl(command);
        this.terminationCommand = terminationCommand;
    }

    @Override
    public void execute() {
        command.execute();
    }

    @Override
    public void stop() {
        command.changeCommand(terminationCommand);
    }
}
