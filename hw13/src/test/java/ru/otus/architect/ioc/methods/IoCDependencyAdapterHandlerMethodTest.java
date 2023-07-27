package ru.otus.architect.ioc.methods;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.Game;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.ioc.plugins.gen.GameObjectMovableAdapterPlugin;
import ru.otus.architect.ioc.storages.IoCStorage;
import ru.otus.architect.ioc.storages.IoCThreadLocalStorage;
import ru.otus.architect.processor.DependencyNameGeneratorImpl;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IoCDependencyAdapterHandlerMethodTest {
    private final static String REGISTER = "IoC.Register";
    private final static String SCOPE_NEW = "Scopes.New";
    private final static Class<?> TEST_INTERFACE = Movable.class;

    @Mock
    private Game game;

    private FactoryMethod method;

    @BeforeEach
    void setUp() {
        when(game.getGameId()).thenReturn("");
        IoCStorage storage = new IoCThreadLocalStorage(
                Map.of(
                        REGISTER, IoCRegisterMethod::new,
                        SCOPE_NEW, IoCScopeMethod::new));
        new GameObjectMovableAdapterPlugin(
                storage::put,
                storage::setScope,
                game)
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

    @Test
    @DisplayName("Если количество аргументов не равно 1, то возвращает исключение")
    void createWithNoArgs() {
        assertThrows(RuntimeException.class, () -> method.create());
    }

    @Test
    @DisplayName("Если аргумент имеет тип отличный от Class<?>, то возвращает исключение")
    void createWithNotClassArgs() {
        assertThrows(RuntimeException.class, () -> method.create(new Object()));
    }
}