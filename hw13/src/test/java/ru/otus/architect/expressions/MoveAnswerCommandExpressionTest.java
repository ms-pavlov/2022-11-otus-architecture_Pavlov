package ru.otus.architect.expressions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.architect.api.AnswerConsumer;
import ru.otus.architect.commands.Command;
import ru.otus.architect.game.objects.dimension.vector.Vector;
import ru.otus.architect.game.objects.dimension.vector.VectorDecorator;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoveAnswerCommandExpressionTest {

    private final static List<Integer> COORDINATES = List.of(1);

    @Mock
    private ExpressionContext context;
    @Mock
    private Vector vector;
    @Mock
    private Expression<Vector> vectorExpression;
    @Mock
    private AnswerConsumer answerConsumer;


    private Expression<Command> expression;

    @BeforeEach
    void setUp() {
        expression = new MoveAnswerCommandExpression(vectorExpression);
    }

    @Test
    void interpret() {
        when(vectorExpression.interpret(context)).thenReturn(vector);
        when(vector.getCoordinates()).thenReturn(COORDINATES);
        when(context.getAnswerConsumer()).thenReturn(answerConsumer);

        expression.interpret(context).execute();

        verify(answerConsumer, times(1))
                .accept(new VectorDecorator(vector).getCoordinatesAsString());
    }
}