package ru.otus.architect.commands;

import ru.otus.architect.game.objects.characteristic.AngularAccelerator;

public class AngularAccelerationCommand implements Command{

    private final AngularAccelerator angularAccelerator;

    public AngularAccelerationCommand(AngularAccelerator angularAccelerator) {
        this.angularAccelerator = angularAccelerator;
    }

    @Override
    public void execute() {
        try {
            angularAccelerator.setAngularVelocity(
                    angularAccelerator.getAngularVelocity()
                            .add(angularAccelerator.getAngularAcceleration()));
        } catch (Exception exception) {
            throw new AngularAccelerationCommandException(exception);
        }

    }
}
