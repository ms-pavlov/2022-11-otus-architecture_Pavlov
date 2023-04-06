package ru.otus.architect.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.processor.DependencyNameGeneratorFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdapterInvocationHandlerProviderImplTest {

    private final static Object TEST_OBJECT = new Object();

    @Mock
    private IoCContainer container;
    @Mock
    private DependencyNameGeneratorFactory generatorFactory;

    private InvocationHandlerProvider provider;

    @BeforeEach
    void setUp() {
        provider = new AdapterInvocationHandlerProviderImpl(container, generatorFactory);
    }

    @Test
    void getHandler() {
        var result = provider.getHandler(TEST_OBJECT);

        assertNotNull(result);
        assertEquals(AdapterInvocationHandler.class, result.getClass());

    }
}