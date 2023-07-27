package ru.otus.architect.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.game.objects.dimension.vector.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovablePositionExpressionTest {

    @Mock
    private ExpressionContext context;
    @Mock
    private Movable movable;
    @Mock
    private Vector vector;
    @Mock
    private Expression<Movable> movableExpression;

    private Expression<Vector> expression;

    @BeforeEach
    void setUp() {
        expression = new MovablePositionExpression(movableExpression);
    }

    @Test
    void interpret() {
        when(movableExpression.interpret(context)).thenReturn(movable);
        when(movable.getPosition()).thenReturn(vector);

        var result = expression.interpret(context);

        assertEquals(vector, result);
    }
}