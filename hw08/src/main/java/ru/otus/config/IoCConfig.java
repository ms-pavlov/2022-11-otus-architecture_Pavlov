package ru.otus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.architect.api.AnswerConsumer;
import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.InterpretCommand;
import ru.otus.architect.commands.MoveCommand;
import ru.otus.architect.commands.macrocommands.TransactionalMacroCommandChange;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.game.objects.dimension.vector.Vector;
import ru.otus.architect.game.objects.dimension.vector.VectorDecorator;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.methods.IoCRegisterMethod;
import ru.otus.architect.ioc.methods.IoCScopeMethod;
import ru.otus.architect.ioc.storages.IoCStorage;
import ru.otus.architect.ioc.storages.IoCThreadLocalStorage;
import ru.otus.openapi.model.Message;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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

    @Bean(name = "scopeIoCStrategy")
    public Consumer<String> scopeIoCStrategy(IoCContainer container) {
        return name -> {
            container.resolve(SCOPE_NEW, name);
            log.info("selected scope {}  in IoCContainer", name);
        };
    }

    @Bean(name = "dependencyIoCStrategy")
    public BiFunction<String, Object[], Object> dependencyIoCStrategy(IoCContainer container) {
        return container::resolve;
    }

    @Bean(name = "commandBuilderStorage")
    public Map<String, BiFunction<Message, AnswerConsumer, Command>> commandBuilderStorage(
            IoCContainer container,
            @Qualifier("commandConsumerStrategy") BiConsumer<String, Command> commandConsumerStrategy) {
        Map<String, BiFunction<Message, AnswerConsumer, Command>> result = new HashMap<>();
        result.put("Game.MoveCommand", (message, answerConsumer) -> {
            selectScore(message, container);
            Movable obj = (Movable) container.resolve(
                    "Adapter",
                    Movable.class,
                    container.resolve("IoC.Game::getGameObject", message));
            return new TransactionalMacroCommandChange(
                    List.of(new MoveCommand(obj),
                            () -> {
                                    var position = new VectorDecorator(obj.getPosition());
                                    answerConsumer.accept(position.getCoordinatesAsString());
                            }));
        });
        result.put("Message.InterpretCommand", (message, answerConsumer) -> {
            selectScore(message, container);
            return new InterpretCommand(
                    commandConsumerStrategy,
                    value -> (Command) container.resolve(
                            (String) container.resolve("IoC.Game::getActionName", message),
                            message,
                            answerConsumer),
                    message);
        });
        return result;
    }

    private void selectScore(Message message, IoCContainer container) {
        container.resolve(
                SCOPE_NEW,
                Optional.ofNullable(message)
                        .map(Message::getGame)
                        .map(UUID::toString)
                        .orElseThrow(RuntimeException::new));
    }

}
