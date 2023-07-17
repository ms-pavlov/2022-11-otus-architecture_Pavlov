package ru.otus.architect.commands;

import org.springframework.stereotype.Component;
import ru.otus.architect.api.AnswerConsumer;
import ru.otus.architect.expressions.ExpressionContext;
import ru.otus.architect.expressions.ExpressionContextFactory;
import ru.otus.openapi.model.Message;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class CommandFactoryImpl implements CommandFactory {

    private final Map<String, Function<ExpressionContext, Command>> commandBuilderStorage;
    private final ExpressionContextFactory expressionContextFactory;


    public CommandFactoryImpl(
            Map<String, Function<ExpressionContext, Command>> commandBuilderStorage,
            ExpressionContextFactory expressionContextFactory) {
        this.commandBuilderStorage = commandBuilderStorage;
        this.expressionContextFactory = expressionContextFactory;
    }

    @Override
    public Command create(String name, Message message, AnswerConsumer consumer) {
        return Optional.ofNullable(name)
                .map(commandBuilderStorage::get)
                .map(builder -> builder.apply(
                        expressionContextFactory.create(message, consumer)))
                .orElseThrow(RuntimeException::new);
    }
}
