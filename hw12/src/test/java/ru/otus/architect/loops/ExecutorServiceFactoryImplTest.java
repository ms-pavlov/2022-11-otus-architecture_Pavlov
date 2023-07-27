package ru.otus.architect.loops;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExecutorServiceFactoryImplTest {
    private static final int POOL_SIZE = 1;
    private static final int COUNT = 100;

    ExecutorServiceFactoryImplTest() {
    }

    @Test
    @DisplayName("Содает ExecutorService который может запускать обработку в потоке из пула")
    void getExecutorService() throws ExecutionException, InterruptedException, TimeoutException {
        NavigableSet<String> names = new ConcurrentSkipListSet<>();
        ExecutorServiceFactory factory = new ExecutorServiceFactoryImpl("executor-", 1);
        ExecutorService executorService = factory.getExecutorService();

        for(int i = 0; i < 100; ++i) {
            executorService.submit(() -> names.add(Thread.currentThread().getName())).get(1L, TimeUnit.SECONDS);
        }

        Assertions.assertEquals(1, names.size());
    }
}
