package ru.otus.architect.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameObjectImplTest {
    private final static String TEST_PARAMETER_NAME = "test";
    private final static String TEST_BAD_PARAMETER_NAME = "bad_name";
    private final static String TEST_SET_PARAMETER_NAME = "bad_name";
    private final static Object TEST_PARAMETER_VALUE = new Object();
    private final static Object TEST_SET_PARAMETER_VALUE = new Object();
    private final static Map<String, Object> TEST_PARAMETERS = Map.of(TEST_PARAMETER_NAME, TEST_PARAMETER_VALUE);

    private GameObject obj;

    @BeforeEach
    void setUp() {
        obj = new GameObjectImpl(TEST_PARAMETERS);
    }

    @Test
    void testConstructor() {
        assertDoesNotThrow(() -> new GameObjectImpl());
    }


    @Test
    void getParameter() {
        Object result = obj.getParameter(TEST_PARAMETER_NAME);

        assertEquals(TEST_PARAMETER_VALUE, result);
    }

    @Test
    void getParameterNotExist() {
        Object result = obj.getParameter(TEST_BAD_PARAMETER_NAME);

        assertNull(result);
    }

    @Test
    void setParameterExist() {
        obj.setParameter(TEST_PARAMETER_NAME, TEST_SET_PARAMETER_VALUE);

        assertEquals(TEST_SET_PARAMETER_VALUE,  obj.getParameter(TEST_PARAMETER_NAME));
    }

    @Test
    void setParameterNotExist() {
        assertNull(obj.getParameter(TEST_SET_PARAMETER_NAME));

        obj.setParameter(TEST_SET_PARAMETER_NAME, TEST_SET_PARAMETER_VALUE);

        assertEquals(TEST_SET_PARAMETER_VALUE,  obj.getParameter(TEST_SET_PARAMETER_NAME));
    }
}