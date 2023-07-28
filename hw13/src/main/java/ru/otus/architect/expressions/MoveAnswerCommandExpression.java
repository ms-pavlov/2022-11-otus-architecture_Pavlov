package ru.otus.architect.expressions;

import ru.otus.architect.commands.Command;
import ru.otus.architect.game.objects.dimension.vector.Vector;
import ru.otus.architect.game.objects.dimension.vector.VectorDecorator;

import java.util.HashMap;
import java.util.Optional;

public class MoveAnswerCommandExpression implements Expression<Command> {
    private final Expression<Vector> expression;

    public MoveAnswerCommandExpression(Expression<Vector> expression) {
        this.expression = expression;
    }

    @Override
    public Command interpret(ExpressionContext context) {
        return () -> Optional.of(context)
                .map(ExpressionContext::getAnswerConsumer)
                .ifPresent(
                        answerConsumer -> answerConsumer.accept(
                                Optional.ofNullable(expression.interpret(context))
                                        .map(VectorDecorator::new)
                                        .map(VectorDecorator::getCoordinatesAsString)
                                        .orElseGet(HashMap::new)));
    }
}
