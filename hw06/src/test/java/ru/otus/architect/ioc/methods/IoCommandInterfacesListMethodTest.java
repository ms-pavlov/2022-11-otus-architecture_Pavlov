package ru.otus.architect.ioc.methods;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.architect.commands.macro.StoppableMoveCommand;
import ru.otus.architect.game.objects.characteristic.Movable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IoCommandInterfacesListMethodTest {

    private final static String TEST_COMMAND = StoppableMoveCommand.class.getName();
    private final static Class<?> TEST_INTERFACE = Movable.class;

    private FactoryMethod method;

    @BeforeEach
    void setUp() {
        method = new IoCommandInterfacesListMethod();
    }

    @Test
    @DisplayName("Возвращает список типов параметров для констурктора")
    void create() {
        List<Class<?>> result = (List<Class<?>>) method.create(TEST_COMMAND);

        assertEquals(1, result.size());
        assertTrue(result.contains(TEST_INTERFACE));
    }

    @Test
    @DisplayName("Возвращает исключение если неверно задано имя команды")
    void createException() {
        assertThrows(RuntimeException.class, () -> method.create());
    }

}