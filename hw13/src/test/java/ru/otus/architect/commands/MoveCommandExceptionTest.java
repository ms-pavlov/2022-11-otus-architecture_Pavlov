package ru.otus.architect.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MoveCommandExceptionTest {

    private Throwable throwable;

    @Test
    void constructorTest() {
        assertDoesNotThrow(() -> new MoveCommandException(throwable));
    }

}