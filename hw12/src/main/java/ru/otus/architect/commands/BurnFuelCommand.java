package ru.otus.architect.commands;

import ru.otus.architect.game.objects.characteristic.FuelConsumer;

public class BurnFuelCommand implements Command {

    private final FuelConsumer fuelConsumer;

    public BurnFuelCommand(FuelConsumer fuelConsumer) {
        this.fuelConsumer = fuelConsumer;
    }


    @Override
    public void execute() {
        int newFuelLvl = fuelConsumer.getFuelLevel() - fuelConsumer.getConsumption();
        if (newFuelLvl < 0) {
            throw new RuntimeException("Fuel level too low");
        }
        fuelConsumer.setFuelLvl(newFuelLvl);
    }
}
