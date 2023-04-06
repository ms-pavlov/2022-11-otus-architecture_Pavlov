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
class IoCScopeMethodTest {

    private final static String TEST_SCOPE_NAME = "name";

    @Mock
    private IoCStorage storage;

    private FactoryMethod method;

    @BeforeEach
    void setUp() {
        method = new IoCScopeMethod(storage);
    }

    @Test
    @DisplayName("Плагин добавляет Scope")
    void execute() {
        method.create(TEST_SCOPE_NAME);

        verify(storage, times(1)).setScope(TEST_SCOPE_NAME);
    }

    @Test
    @DisplayName("Если неверно указаны параметры возвращает исключение")
    void executeException() {
        assertThrows(RuntimeException.class, () -> method.create());
        assertThrows(RuntimeException.class, () -> method.create(TEST_SCOPE_NAME, TEST_SCOPE_NAME));
        assertThrows(RuntimeException.class, () -> method.create(1));
    }
}