package ru.otus.architect.ioc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.storages.IoCStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IoCContainerImplTest {

    private final static String TEST_NAME = "test";
    private final static Object TEST_OBJECT = new Object();
    @Mock
    private IoCStorage storage;
    @Mock
    private FactoryMethod method;

    private IoCContainer container;

    @BeforeEach
    void setUp() {
        when(storage.get(TEST_NAME)).thenReturn(method);
        when(method.create()).thenReturn(TEST_OBJECT);
        container = new IoCContainerImpl(storage);
    }



    @Test
    @DisplayName("Должен разрешать зависимость по имени")
    void resolve() {
        var result = container.resolve(TEST_NAME);

        assertEquals(TEST_OBJECT, result);
    }

}