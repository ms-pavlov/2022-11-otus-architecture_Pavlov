package ru.otus.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.mesh.PlayingField;
import ru.otus.objects.Crossable;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrossCommandTest {

    @Mock
    private PlayingField field1;
    @Mock
    private PlayingField field2;
    @Mock
    private Command fieldCommand1;
    @Mock
    private Command fieldCommand2;
    @Mock
    private Crossable crossable;
    @Mock
    private BiFunction<PlayingField, Crossable, Command> chainCrossCommandStrategy;

    private Command command;
    private Map<PlayingField, Command > fields;

    @BeforeEach
    void setUp() {
        fields = Map.of(
                field1, fieldCommand1,
                field2, fieldCommand2);
        fields.forEach(
                (playingField, fieldCommand) ->  when(chainCrossCommandStrategy.apply(playingField, crossable)).thenReturn(fieldCommand));
        command = new CrossCommand(fields.keySet().stream().toList(), crossable, chainCrossCommandStrategy);
    }

    @Test
    @DisplayName("При выполнении делает проверку для каждого поля")
    void execute() {
        command.execute();

        fields.forEach(
                (playingField, fieldCommand) ->  verify(
                        chainCrossCommandStrategy,
                        times(1))
                        .apply(playingField, crossable));

        fields.values().forEach(value -> verify(value, times(1)).execute());
    }
}