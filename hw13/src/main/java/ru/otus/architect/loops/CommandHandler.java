package ru.otus.architect.loops;

import ru.otus.architect.commands.Command;

import java.util.function.Consumer;

public interface CommandHandler extends Consumer<Command> {
}