package ru.otus.architect.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IoCPluginExceptionTest {

    @Test
    void testConstructor() {
        assertDoesNotThrow(IoCPluginException::new);
    }
}