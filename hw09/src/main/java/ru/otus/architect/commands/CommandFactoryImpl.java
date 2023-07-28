package ru.otus.architect.commands;

import org.springframework.stereotype.Component;
import ru.otus.architect.api.AnswerConsumer;
import ru.otus.openapi.model.Message;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
public class CommandFactoryImpl implements CommandFactory {

    private final Map<String, BiFunction<Message, AnswerConsumer, Command>> commandBuilderStorage;


    public CommandFactoryImpl(Map<String, BiFunction<Message, AnswerConsumer, Command>> commandBuilderStorage) {
        this.commandBuilderStorage = commandBuilderStorage;
    }

    @Override
    public Command create(String name, Message message, AnswerConsumer consumer) {
        return Optional.ofNullable(name)
                .map(commandBuilderStorage::get)
                .map(builder -> builder.apply(message, consumer))
                .orElseThrow(RuntimeException::new);
    }
}
