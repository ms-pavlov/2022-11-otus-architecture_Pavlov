package ru.otus.architect.ioc.methods;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.architect.commands.macro.StoppableMoveCommand;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.IoCContainerImpl;
import ru.otus.architect.ioc.storages.IoCStorage;
import ru.otus.architect.ioc.storages.IoCThreadLocalStorage;
import ru.otus.architect.processor.DependencyNameGeneratorImpl;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IoCDependencyAdapterHandlerMethodTest {
    private final static String REGISTER = "IoC.Register";
    private final static String SCOPE_NEW = "Scopes.New";
    private final static Class<?> TEST_COMMAND = StoppableMoveCommand.class;
    private final static Class<?> TEST_INTERFACE = Movable.class;

    private FactoryMethod method;

    @BeforeEach
    void setUp() {
        IoCStorage storage = new IoCThreadLocalStorage(
                Map.of(
                        REGISTER, IoCRegisterMethod::new,
                        SCOPE_NEW, IoCScopeMethod::new));
        IoCContainer container = new IoCContainerImpl(storage);

        new ru.otus.architect.ioc.plugins.gen.StarShipMovableAdapterPlugin(
                REGISTER,
                SCOPE_NEW,
                container)
                .execute();

        method = new IoCDependencyAdapterHandlerMethod(
                storage,
                DependencyNameGeneratorImpl::new);
    }


    @Test
    @DisplayName("Проверяет зарегестрированные зависимости для интерфейса")
    void create() {
        assertTrue((Boolean) method.create(TEST_INTERFACE));

    }
}