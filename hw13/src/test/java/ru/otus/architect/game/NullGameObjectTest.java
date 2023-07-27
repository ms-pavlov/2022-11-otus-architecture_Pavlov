package ru.otus.architect.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NullGameObjectTest {

    private GameObject gameObject;

    @BeforeEach
    void setUp() {
        gameObject = new NullGameObject();
    }

    @Test
    void getParameter() {
        var result = gameObject.getParameter("");

        assertNull(result);
    }

    @Test
    void setParameter() {
        assertThrows(RuntimeException.class, () -> gameObject.setParameter("", null));
    }
}