package ru.otus.architect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExampleTest {

    @Test
    void main() {
        assertDoesNotThrow(() -> Example.main());
    }
}