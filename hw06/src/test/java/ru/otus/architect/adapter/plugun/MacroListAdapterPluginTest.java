package ru.otus.architect.adapter.plugun;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.plugun.IoCPlugin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MacroListAdapterPluginTest {
    private final static String DEPENDENCY_COMMAND = "IoC.Adapter::checkCommandInterface";
    public final static String REGISTER = "IoC.Register";
    public final static String SCOPE_NEW = "Scopes.New";
    private final static String DEPENDENCY_INTERFACE_LIST = "IoC::getCommandInterface";
    private final static String DEPENDENCY_ADAPTER_HANDLER = "IoC.Adapter::Contains";
    public final static String SCOPE = "main";
    @Mock
    private IoCContainer container;

    private IoCPlugin plugin;

    @BeforeEach
    void setUp() {
        plugin = new MacroListAdapterPlugin(
                REGISTER,
                SCOPE_NEW,
                SCOPE,
                DEPENDENCY_INTERFACE_LIST,
                DEPENDENCY_ADAPTER_HANDLER,
                container);
    }

    @Test
    @DisplayName("Регистрирует зависимость для проверки, что определены все необходимые зависимости")
    void execute() {
        plugin.execute();

        verify(container, times(1)).resolve(eq(SCOPE_NEW), eq(SCOPE));
        verify(container, times(1)).resolve(eq(REGISTER), eq(DEPENDENCY_COMMAND), any());
    }
}