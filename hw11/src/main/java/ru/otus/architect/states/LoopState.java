package ru.otus.architect.states;

import ru.otus.architect.commands.Command;
import ru.otus.architect.loops.CommandHandler;

import java.util.concurrent.BlockingQueue;

public interface LoopState extends CommandHandler {

    default void doDefault() {
    }

    default void moveTo(BlockingQueue<Command> queue) {
    }

    default void stop() {
    }

    LoopState next();

}
