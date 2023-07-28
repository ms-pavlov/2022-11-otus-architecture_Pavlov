package ru.otus.architect.loops;

import lombok.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class SimplerThreadFactory implements ThreadFactory {
    private final AtomicLong threadIdGenerator = new AtomicLong(0L);
    private final String namePrefix;

    public SimplerThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(@NonNull Runnable task) {
        var thread = new Thread(task);
        thread.setName(namePrefix + threadIdGenerator.incrementAndGet());
        return thread;
    }
}