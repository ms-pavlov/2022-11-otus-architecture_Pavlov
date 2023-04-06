package ru.otus.architect.commands;

import ru.otus.architect.game.objects.characteristic.Accelerator;

public class BoostCommand implements Command {

    private final Accelerator accelerator;

    public BoostCommand(Accelerator accelerator) {
        this.accelerator = accelerator;
    }

    @Override
    public void execute() {
        try {
            accelerator.setVelocity(accelerator.getVelocity().scalarAdd(accelerator.getAcceleration()));
        } catch (Exception exception) {
            throw new BoostCommandException(exception);
        }
    }
}
