package ru.otus.architect.loops;

import java.util.function.Consumer;
import ru.otus.architect.commands.Command;

public interface CommandHandler extends Consumer<Command> {
}