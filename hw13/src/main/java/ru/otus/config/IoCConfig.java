package ru.otus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.InterpretCommand;
import ru.otus.architect.expressions.*;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.methods.IoCRegisterMethod;
import ru.otus.architect.ioc.methods.IoCScopeMethod;
import ru.otus.architect.ioc.storages.IoCStorage;
import ru.otus.architect.ioc.storages.IoCThreadLocalStorage;
import ru.otus.openapi.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class IoCConfig {

    private static final Logger log = LoggerFactory.getLogger(IoCConfig.class);
    public final static String REGISTER = "IoC.Register";
    public final static String SCOPE_NEW = "Scopes.New";

    @Bean
    @Scope("singleton")
    public IoCStorage ioCStorage() {
        return new IoCThreadLocalStorage(Map.of(REGISTER, IoCRegisterMethod::new, SCOPE_NEW, IoCScopeMethod::new));
    }

    @Bean(name = "registerIoCStrategy")
    public BiConsumer<String, FactoryMethod> registerIoCStrategy(IoCContainer container) {
        return (name, method) -> {
            container.resolve(REGISTER, name, method);
            log.info("dependency {} registered in IoCContainer", name);
        };
    }

    @Bean(name = "dependencyIoCStrategy")
    public BiFunction<String, Object[], Object> dependencyIoCStrategy(IoCContainer container) {
        return container::resolve;
    }

    @Bean(name = "scopeIoCStrategy")
    public Consumer<String> scopeIoCStrategy(IoCContainer container) {
        return name -> {
            container.resolve(SCOPE_NEW, name);
            log.info("selected scope {}  in IoCContainer", name);
        };
    }

    @Bean(name = "commandBuilderStorage")
    public Map<String, Function<ExpressionContext, Command>> commandBuilderStorage(
            @Qualifier("commandConsumerStrategy") BiConsumer<String, Command> commandConsumerStrategy) {
        Map<String, Function<ExpressionContext, Command>> result = new HashMap<>();
        result.put(
                "Game.MoveCommand",
                expressionContext -> {
                    selectScore(expressionContext);
                    return new MacroCommandExpression(
                            new MoveCommandExpression(
                                    new AdapterExpression<>(
                                            Movable.class,
                                            new GameObjectExpression())),
                            new MoveAnswerCommandExpression(
                                    new MovablePositionExpression(
                                            new AdapterExpression<>(
                                                    Movable.class,
                                                    new GameObjectExpression()))))
                            .interpret(expressionContext);
                });
        result.put("Message.InterpretCommand",
                expressionContext -> {
                    selectScore(expressionContext);
                    return new InterpretCommand(
                            commandConsumerStrategy,
                            value -> new CommandExpression(new ActionNameExpression())
                                    .interpret(expressionContext),
                            expressionContext.getMessage());
                });
        return result;
    }

    @Bean(name = "expressionContextFactory")
    public ExpressionContextFactory expressionContextFactory(IoCContainer container) {
        return (message, consumer) -> new ExpressionContextImpl(
                container::resolve,
                message,
                consumer);
    }

    private void selectScore(ExpressionContext expressionContext) {
        expressionContext.resolve(
                SCOPE_NEW,
                Optional.ofNullable(expressionContext.getMessage())
                        .map(Message::getGame)
                        .map(UUID::toString)
                        .orElseThrow(RuntimeException::new));
    }

}
