package ru.otus.architect.commands.macro;

import ru.otus.architect.commands.Command;
import ru.otus.architect.exceptions.handlers.MacroCommandExceptionHandler;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class MacroCommand implements Command {
    private final Queue<Command> commands;
    private final MacroCommandExceptionHandler exceptionHandler;

    public MacroCommand(List<Command> commands) {
        this.commands = new ArrayDeque<>(commands);
        exceptionHandler = new MacroCommandExceptionHandler(this.commands);
    }

    @Override
    public void execute() {
        while (!commands.isEmpty()) {
            var cmd = commands.poll();
            try {
                cmd.execute();
            } catch (Exception ex) {
                exceptionHandler.handle(cmd, ex);
            }
        }
    }
}
