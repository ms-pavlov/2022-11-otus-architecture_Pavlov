package ru.otus.architect.commands.macro;

import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.MoveCommand;
import ru.otus.architect.commands.RetryCommand;
import ru.otus.architect.commands.macrocommands.TransactionalMacroCommandChange;
import ru.otus.architect.commands.stoppable.StoppableCommand;
import ru.otus.architect.commands.stoppable.StoppableCommandImpl;
import ru.otus.architect.commands.transactions.TransactionalImpl;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.game.objects.dimension.Coordinates;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class StoppableMoveMacroCommand implements StoppableCommand {

    private final Stack<Coordinates> history;
    private final StoppableCommand command;

    private int steps;

    public StoppableMoveMacroCommand(
            Movable movable,
            int steps,
            Queue<Command> commandQueue,
            Command terminalCommand) {
        this.steps = steps;
        history = new Stack<>();
        command = new StoppableCommandImpl(
                new TransactionalMacroCommandChange(new LinkedList<>(
                        List.of(
                                new TransactionalImpl(
                                        new MoveCommand(movable),
                                        () -> history.push(movable.getPosition()),
                                        () -> movable.setPosition(history.pop())
                                ),
                                new RetryCommand(this, commandQueue)
                        )
                )),
                terminalCommand);
    }

    @Override
    public void execute() {
        if (steps <= 0) {
            this.stop();
        }
        this.steps--;
        command.execute();

    }

    @Override
    public void stop() {
        command.stop();
    }
}
