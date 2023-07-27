package ru.otus.architect.commands;

import ru.otus.openapi.model.Message;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class InterpretCommand implements Command {

    private final BiConsumer<String, Command> commandConsumerStrategy;
    private final Function<Message, Command> commandBuilderStrategy;
    private final Message message;

    public InterpretCommand(
            BiConsumer<String, Command> commandConsumerStrategy,
            Function<Message, Command> commandBuilderStrategy,
            Message message) {
        this.commandConsumerStrategy = commandConsumerStrategy;
        this.commandBuilderStrategy = commandBuilderStrategy;
        this.message = message;
    }

    @Override
    public void execute() {
        commandConsumerStrategy.accept(
                Optional.ofNullable(message)
                        .map(Message::getGame)
                        .map(UUID::toString)
                        .orElseThrow(RuntimeException::new),
                commandBuilderStrategy.apply(message));
    }
}
