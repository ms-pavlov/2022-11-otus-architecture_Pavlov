package ru.otus.architect.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.StarShip;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.IoCContainerImpl;
import ru.otus.architect.ioc.methods.IoCRegisterMethod;
import ru.otus.architect.ioc.methods.IoCScopeMethod;
import ru.otus.architect.ioc.storages.IoCThreadLocalStorage;
import ru.otus.architect.processor.DependencyNameGeneratorImpl;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdapterFactoryImplTest {

    public final static String REGISTER = "IoC.Register";
    public final static String SCOPE_NEW = "Scopes.New";

    private final static Object TEST_OBJECT = new Object();
    private final static Class<?> TEST_INTERFACE = Movable.class;
    @Mock
    private AdapterGenerator generator;

    private AdapterFactory adapterFactory;

    @BeforeEach
    void setUp() {
        adapterFactory = new AdapterFactoryImpl(generator);
    }

    @Test
    @DisplayName("Создает адаптер")
    void getAdapter() {
        when(generator.generate(TEST_INTERFACE, TEST_OBJECT)).thenReturn(TEST_OBJECT);

        var result = adapterFactory.getAdapter(TEST_INTERFACE, TEST_OBJECT);

        assertEquals(TEST_OBJECT, result);
        verify(generator, times(1)).generate(TEST_INTERFACE, TEST_OBJECT);
    }

    @Test
    @DisplayName("Созданные адаптеры кешируются")
    void getAdapterCash() {
        when(generator.generate(TEST_INTERFACE, TEST_OBJECT)).thenReturn(TEST_OBJECT);

        var result1 = adapterFactory.getAdapter(TEST_INTERFACE, TEST_OBJECT);
        var result2 = adapterFactory.getAdapter(TEST_INTERFACE, TEST_OBJECT);

        assertEquals(TEST_OBJECT, result1);
        assertEquals(TEST_OBJECT, result2);
        verify(generator, times(1)).generate(TEST_INTERFACE, TEST_OBJECT);
    }

    @Test
    @DisplayName("Интеграционный тест")
    void getAdapterIntegration() {
        IoCContainer container = new IoCContainerImpl(
                new IoCThreadLocalStorage(
                        Map.of(
                                REGISTER, IoCRegisterMethod::new,
                                SCOPE_NEW, IoCScopeMethod::new)));
        new ru.otus.architect.ioc.plugins.gen.StarShipMovableAdapterPlugin(
                "IoC.Register",
                "Scopes.New",
                container).execute();

        AdapterFactory factory = new AdapterFactoryImpl(
                new AdapterGeneratorImpl(
                        new AdapterInvocationHandlerProviderImpl(container, DependencyNameGeneratorImpl::new)));


        Movable movable = (Movable) factory.getAdapter(Movable.class, new StarShip());
        assertThrows(RuntimeException.class, movable::getPosition, "Not support yet!");
    }

}