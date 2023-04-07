package ru.otus.architect.loops;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimplerThreadFactoryTest {
    private static final String TEST_MSG = "Ок";
    private final List<String> test = new ArrayList<>();

    SimplerThreadFactoryTest() {
    }

    @Test
    @DisplayName("Создает поток")
    void newThread() throws InterruptedException {
        Thread thread = (new SimplerThreadFactory("test")).newThread(() -> this.test.add("Ок"));
        thread.start();
        thread.join();
        Assertions.assertEquals(1, this.test.size());
        Assertions.assertEquals(TEST_MSG, this.test.get(0));
    }

}