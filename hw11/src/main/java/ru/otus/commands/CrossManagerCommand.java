package ru.otus.commands;

import ru.otus.objects.Crossable;

public class CrossManagerCommand implements Command{

    private final Crossable crossable1;
    private final Crossable crossable2;
    private final Command onCrossCommand;

    public CrossManagerCommand(
            Crossable crossable1,
            Crossable crossable2,
            Command onCrossCommand) {
        this.crossable1 = crossable1;
        this.crossable2 = crossable2;
        this.onCrossCommand = onCrossCommand;
    }

    @Override
    public void execute() {
        if(crossable1.hasCrossing(crossable2)) {
            onCrossCommand.execute();
        }
    }
}
