package ru.otus.architect.commands.macro;


import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.stoppable.Stoppable;
import ru.otus.architect.game.objects.characteristic.Movable;

public class StoppableMoveCommand implements Command, Stoppable {
    private final Movable m;
    private boolean stopped;

    public StoppableMoveCommand(Movable m) {
        this.m = m;
    }

    @Override
    public void execute() {
        if (!stopped) {
            m.setPosition(m.getPosition().plus(m.getVelocity()));
        }
    }

    @Override
    public void stop() {
        stopped = true;
    }
}
