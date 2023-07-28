package ru.otus.architect.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.commands.Command;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.game.objects.dimension.vector.Vector;
import ru.otus.architect.game.objects.dimension.vector.Vector2DBuilder;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoveCommandExpressionTest {

    private final static Vector POSITION = Vector2DBuilder.builder().x(0).y(0).build();
    private final static Vector VELOCITY = Vector2DBuilder.builder().x(0).y(0).build();

    @Mock
    private ExpressionContext context;

    @Mock
    private Movable movable;

    @Mock
    private Expression<Movable> movableExpression;

    private Expression<Command> expression;

    @BeforeEach
    void setUp() {
        expression = new MoveCommandExpression(movableExpression);
    }

    @Test
    void interpret() {
        when(movableExpression.interpret(context)).thenReturn(movable);
        when(movable.getPosition()).thenReturn(POSITION);
        when(movable.getVelocity()).thenReturn(VELOCITY);

        var result = expression.interpret(context);
        result.execute();

        assertInstanceOf(Command.class, expression.interpret(context));
        verify(movable, times(1)).getPosition();
        verify(movable, times(1)).getVelocity();
        verify(movable, times(1)).setPosition(any());
    }
}