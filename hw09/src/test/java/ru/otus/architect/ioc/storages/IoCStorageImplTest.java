package ru.otus.architect.ioc.storages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.ioc.methods.FactoryMethod;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IoCStorageImplTest {
    private final static Logger log = LoggerFactory.getLogger(IoCStorageImplTest.class);

    public final static String REGISTER = "IoC.Register";
    public final static String SCOPE_NEW = "Scopes.New";
    private final static String METHOD_NAME = "test";
    private final static String SCOPE_NAME = "test";
    private final static String TEST_DEPENDENCY = "IoC.Object.test";
    private final static String TEST_DEPENDENCY_REGEX = "^IoC.\\S*.test$";
    private final static String SCOPE_METHOD_NAME = SCOPE_NAME + "_" + METHOD_NAME;

    @Mock
    private FactoryMethod method;
    @Mock
    private FactoryMethod method2;
    @Mock
    private FactoryMethod register;
    @Mock
    private FactoryMethod scope;

    private IoCStorage storage;

    @BeforeEach
    void setUp() {
        storage = new IoCThreadLocalStorage(
                Map.of(
                        REGISTER, storage1 -> register,
                        SCOPE_NEW, storage1 -> scope));
    }

    @Test
    @DisplayName("Можно менять Scope")
    void setScope() {
        storage.put(METHOD_NAME, method);

        storage.setScope(SCOPE_NAME);
        storage.put(SCOPE_METHOD_NAME, method2);

        assertThrows(RuntimeException.class, () -> storage.get(METHOD_NAME));
        assertEquals(method2, storage.get(SCOPE_METHOD_NAME));
    }

    @Test
    @DisplayName("По-умолчанию есть регистрация")
    void hasRegister() {
        assertEquals(register, storage.get(REGISTER));
        storage.setScope(SCOPE_NAME);
        assertEquals(register, storage.get(REGISTER));
    }

    @Test
    @DisplayName("По-умолчанию есть смена Scope")
    void hasScope() {
        assertEquals(scope, storage.get(SCOPE_NEW));
        storage.setScope(SCOPE_NAME);
        assertEquals(scope, storage.get(SCOPE_NEW));
    }


    @Test
    @DisplayName("Есть Scope по умолчанию")
    void setDefaultScope() {
        storage.setDefaultScope();
        storage.put(METHOD_NAME, method);
        storage.setScope(SCOPE_NAME);
        storage.put(SCOPE_METHOD_NAME, method2);

        storage.setDefaultScope();

        assertEquals(method, storage.get(METHOD_NAME));
        assertThrows(RuntimeException.class, () -> storage.get(SCOPE_METHOD_NAME));
    }

    @Test
    @DisplayName("Если метода с таким имененм нет, вернет исключение")
    void get() {
        assertThrows(RuntimeException.class, () -> storage.get(METHOD_NAME));
    }

    @Test
    @DisplayName("Метод можно добавить и получить")
    void putAndGet() {
        storage.put(METHOD_NAME, method);

        assertEquals(method, storage.get(METHOD_NAME));
    }

    @Test
    @DisplayName("Проверяет наличие зависимости по regex")
    void contains() {
        storage.put(TEST_DEPENDENCY, method);

        assertTrue(storage.contains(TEST_DEPENDENCY_REGEX));

    }

    @RepeatedTest(10)
    @DisplayName("Хранилище патоко безопасно")
    void resolveParallel() {
        var numberOfThreads = 1000;
        var threads = new ArrayList<Thread>();
        var latch = new CountDownLatch(1);

        for (int i = 0; i < numberOfThreads; i++) {
            var thread = new Thread(() -> {
                awaitLatch(latch);

                storage.setDefaultScope();
                storage.put(METHOD_NAME, method);
                storage.setScope(SCOPE_NAME);
                storage.put(SCOPE_METHOD_NAME, method2);

                storage.setDefaultScope();
                storage.get(METHOD_NAME).create();

                storage.setScope(SCOPE_NAME);
                storage.get(SCOPE_METHOD_NAME).create();
            });
            thread.start();
            threads.add(thread);
        }
        latch.countDown();
        threads.forEach(this::joinThread);

        verify(method, times(numberOfThreads)).create();
        verify(method2, times(numberOfThreads)).create();
    }

    private void awaitLatch(CountDownLatch latch) {
        try {
            var result = latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(exception);
        }
    }

    private void joinThread(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }
}