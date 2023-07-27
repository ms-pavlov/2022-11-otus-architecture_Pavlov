package ru.otus.architect.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class IoCPluginExceptionTest {

    @Test
    void testConstructor() {
        assertDoesNotThrow(IoCPluginException::new);
    }
}