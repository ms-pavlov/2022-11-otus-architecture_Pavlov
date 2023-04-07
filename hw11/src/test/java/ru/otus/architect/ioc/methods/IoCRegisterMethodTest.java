package ru.otus.architect.ioc.methods;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.ioc.storages.IoCStorage;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IoCRegisterMethodTest {
    private final static String TEST_NAME = "name";

    @Mock
    private IoCStorage iocStorage;
    @Mock
    private FactoryMethod factoryMethod;

    private FactoryMethod iocRegister;

    @BeforeEach
    void setUp() {
        iocRegister = new IoCRegisterMethod(iocStorage);
    }

    @Test
    @DisplayName("Команда должна регистрировать в IoC новый фабричный метод")
    void execute() {
        iocRegister.create(TEST_NAME, factoryMethod);

        verify(iocStorage, times(1)).put(TEST_NAME, factoryMethod);
    }

    @Test
    @DisplayName("Если количество параметров не равно 2, возвращает исключение")
    void executeNoArgs() {
        assertThrows(RuntimeException.class, () -> iocRegister.create(TEST_NAME));
        assertThrows(RuntimeException.class, () -> iocRegister.create(TEST_NAME, factoryMethod, factoryMethod));
    }

    @Test
    @DisplayName("Если если типы параметров не совпадают, возвращает исключение")
    void executeIncorrectClass() {
        assertThrows(RuntimeException.class, () -> iocRegister.create(TEST_NAME, new Object()));
        assertThrows(RuntimeException.class, () -> iocRegister.create(null, factoryMethod));
    }
}