package ru.otus.architect.expressions;

import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.MoveCommand;
import ru.otus.architect.game.objects.characteristic.Movable;

public class MoveCommandExpression implements Expression<Command> {

    private final Expression<Movable> expression;

    public MoveCommandExpression(Expression<Movable> expression) {
        this.expression = expression;
    }

    @Override
    public Command interpret(ExpressionContext context) {
        return new MoveCommand(
                expression.interpret(context));
    }
}
