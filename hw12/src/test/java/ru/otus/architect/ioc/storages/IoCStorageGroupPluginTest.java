package ru.otus.architect.ioc.storages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IoCStorageGroupPluginTest {
    @Mock
    private IoCStorage storage;
    @Mock
    private IoCStoragePlugin testPlugin;

    private IoCStoragePlugin plugin;

    @BeforeEach
    void setUp() {
        this.plugin = new IoCStorageGroupPlugin(testPlugin);
    }


    @Test
    @DisplayName("Добавляет в хранилище")
    void execute() {
        plugin.execute(storage);

        verify(testPlugin, times(1)).execute(storage);

    }
}