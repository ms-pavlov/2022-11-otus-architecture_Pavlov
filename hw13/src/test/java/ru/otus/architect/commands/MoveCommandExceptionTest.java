package ru.otus.architect.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class MoveCommandExceptionTest {

    @Mock
    private Throwable throwable;

    @Test
    void constructorTest() {
        assertDoesNotThrow(() -> new MoveCommandException(throwable));
    }

}