package ru.otus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.architect.adapter.AdapterFactory;
import ru.otus.architect.adapter.plugins.AdapterPlugin;
import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.CommandFactory;
import ru.otus.architect.exceptions.strategy.ExceptionHandlingStrategyRepository;
import ru.otus.architect.game.Game;
import ru.otus.architect.game.GameObjectStorage;
import ru.otus.architect.game.GameObjectStorageImpl;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.plugins.CommandsPlugin;
import ru.otus.architect.ioc.plugins.GameRegistrationPlugin;
import ru.otus.architect.ioc.plugins.IoCPlugin;
import ru.otus.architect.ioc.plugins.SecurityPlugin;
import ru.otus.architect.ioc.plugins.gen.GameObjectMovableAdapterPlugin;
import ru.otus.architect.loops.QueueLoopThreadImpl;
import ru.otus.architect.processor.DependencyNameGeneratorFactory;
import ru.otus.architect.processor.DependencyNameGeneratorImpl;
import ru.otus.architect.securities.KeyService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class ApplicationConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);
    public final static String SCOPE_NEW = "Scopes.New";
    private final static int DATA_SOURCE_THREAD_POOL_DEFAULT_SIZE = 4;


    @Bean(name = "dependencyNameGeneratorFactory")
    public DependencyNameGeneratorFactory dependencyNameGeneratorFactory() {
        return DependencyNameGeneratorImpl::new;
    }

    @Bean(name = "commandConsumerStrategy")
    public BiConsumer<String, Command> commandConsumerStrategy(IoCContainer container) {
        return (score, command) -> {
            container.resolve(SCOPE_NEW, score);
            container.resolve("IoC.Game::addCommand", command);
        };
    }

    @Bean(name = "commandSupplierStrategy")
    public Function<String, Command> commandSupplierStrategy(IoCContainer container) {
        return score -> {
            container.resolve(SCOPE_NEW, score);
            return (Command) container.resolve("IoC.Game::pollCommand");
        };
    }

    @Bean
    public ExecutorService getDataSourceExecutor() {
        return Executors.newFixedThreadPool(DATA_SOURCE_THREAD_POOL_DEFAULT_SIZE);
    }

    @Bean(name = "gameRegistrationStrategy")
    public Consumer<Game> gameRegistrationStrategy(
            ExecutorService executorService,
            Function<String, Command> commandSupplierStrategy) {
        return game -> executorService.execute(
                new QueueLoopThreadImpl(
                        () -> {
                            var result = commandSupplierStrategy.apply(game.getGameId());
                            Optional.ofNullable(result)
                                    .ifPresent(value -> LOG.info("handle command {}", value));
                            return result;
                        },
                        new ExceptionHandlingStrategyRepository(
                                new HashMap<>(),
                                (cmd1, ex1) -> LOG.error("in command {} error {}", cmd1, ex1)
                        ),
                        Command::execute,
                        () -> true));
    }

    @Bean(name = "gameObjectStorageInitStrategy")
    public Supplier<GameObjectStorage> gameObjectStorageInitStrategy() {
        return GameObjectStorageImpl::new;
    }

    @Bean(name = "defaultGamePlugins")
    public List<Function<Game, IoCPlugin>> defaultGamePlugins(
            @Qualifier("registerIoCStrategy") BiConsumer<String, FactoryMethod> registerIoCStrategy,
            @Qualifier("scopeIoCStrategy") Consumer<String> scopeIoCStrategy,
            AdapterFactory adapterFactory,
            CommandFactory commandFactory,
            KeyService keyService) {
        return List.of(
                value -> new GameObjectMovableAdapterPlugin(registerIoCStrategy, scopeIoCStrategy, value),
                value -> new AdapterPlugin(registerIoCStrategy, scopeIoCStrategy, adapterFactory, value),
                value -> new GameRegistrationPlugin(registerIoCStrategy, scopeIoCStrategy, value),
                value -> new SecurityPlugin(registerIoCStrategy, scopeIoCStrategy, keyService, value),
                value -> new CommandsPlugin(registerIoCStrategy, scopeIoCStrategy, commandFactory, value)
        );
    }
}
