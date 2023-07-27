package ru.otus.architect.securities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final static String TEST_GAME_UUID = "test";
    private final static Long TEST_OBJECT_ID = 1L;
    private final static String USER_NAME = "test1";
    private final static String PASSWORD = "test2";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(USER_NAME, PASSWORD);
    }

    @Test
    void username() {
        assertEquals(USER_NAME, user.username());
    }

    @Test
    void password() {
        assertEquals(PASSWORD, user.password());
    }

    @Test
    void addAndHasAccess() {
        assertFalse(user.hasAccess(TEST_GAME_UUID, TEST_OBJECT_ID));
        assertDoesNotThrow(() -> user.addAccess(TEST_GAME_UUID, TEST_OBJECT_ID));
        assertTrue(user.hasAccess(TEST_GAME_UUID, TEST_OBJECT_ID));
    }

    @Test
    void accesses() {
        assertNotNull(user.accesses());
    }

    @Test
    void getGameAccesses() {
        user.addAccess(TEST_GAME_UUID, TEST_OBJECT_ID);

        var result = user.getGameAccesses(TEST_GAME_UUID);

        assertEquals(List.of(TEST_OBJECT_ID), result);
    }
}