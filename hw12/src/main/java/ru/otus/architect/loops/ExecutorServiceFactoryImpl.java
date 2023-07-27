package ru.otus.architect.loops;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceFactoryImpl implements ExecutorServiceFactory {
    private final String prefix;
    private final int poolSize;

    public ExecutorServiceFactoryImpl(String prefix, int poolSize) {
        this.prefix = prefix;
        this.poolSize = poolSize;
    }

    public ExecutorService getExecutorService() {
        return Executors.newFixedThreadPool(this.poolSize, new SimplerThreadFactory(this.prefix));
    }
}