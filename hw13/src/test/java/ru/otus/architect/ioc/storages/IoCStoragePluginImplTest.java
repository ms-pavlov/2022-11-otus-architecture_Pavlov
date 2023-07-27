package ru.otus.architect.ioc.storages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.ioc.methods.FactoryMethod;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IoCStoragePluginImplTest {

    private final static String TEST_NAME = "name";

    @Mock
    private IoCStorage storage;
    @Mock
    private FactoryMethod method;

    private IoCStoragePlugin plugin;

    @BeforeEach
    void setUp() {
        plugin = new IoCStoragePluginImpl(TEST_NAME, method);
    }

    @Test
    @DisplayName("Добавляет в хранилище")
    void execute() {
        plugin.execute(storage);

        verify(storage, times(1)).put(eq(TEST_NAME), eq(method));
    }
}