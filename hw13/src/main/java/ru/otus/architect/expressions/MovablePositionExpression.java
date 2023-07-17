package ru.otus.architect.expressions;

import ru.otus.architect.game.objects.characteristic.Movable;
import ru.otus.architect.game.objects.dimension.vector.Vector;

import java.util.Optional;

public class MovablePositionExpression implements Expression<Vector> {

    private final Expression<Movable> expression;

    public MovablePositionExpression(Expression<Movable> expression) {
        this.expression = expression;
    }

    @Override
    public Vector interpret(ExpressionContext context) {
        return Optional.ofNullable(context)
                .map(expression::interpret)
                .map(Movable::getPosition)
                .orElse(null);
    }
}
