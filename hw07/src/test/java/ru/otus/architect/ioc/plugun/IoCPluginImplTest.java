package ru.otus.architect.ioc.plugun;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.ioc.IoCContainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IoCPluginImplTest {

    public final static String REGISTER = "IoC.Register";
    public final static String SCOPE_NEW = "Scopes.New";

    @Mock
    private IoCContainer container;

    private IoCPlugin plugin;

    @BeforeEach
    void setUp() {
        plugin = new IoCPluginImpl(REGISTER, SCOPE_NEW, container);
    }

    @Test
    @DisplayName("Добавляет две зависимости в новый scope")
    void execute() {
        plugin.execute();

        verify(container, times(1)).resolve(eq(SCOPE_NEW), any());
        verify(container, times(2)).resolve(eq(REGISTER), any(), any());
    }
}