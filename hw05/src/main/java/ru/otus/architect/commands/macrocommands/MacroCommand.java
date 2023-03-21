package ru.otus.architect.commands.macrocommands;

import java.util.Queue;
import ru.otus.architect.commands.Command;

public class MacroCommand implements Command {

    private final Queue<Command> commandList;

    public MacroCommand(Queue<Command> commandList) {
        this.commandList = commandList;
    }

    @Override
    public void execute() {
        while (!commandList.isEmpty()) {
            try{
                commandList.poll().execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
