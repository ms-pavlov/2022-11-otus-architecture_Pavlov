package ru.otus.architect.expressions;

import ru.otus.architect.commands.Command;

public class CommandExpression implements Expression<Command> {

    private final Expression<String> expression;

    public CommandExpression(Expression<String> expression) {
        this.expression = expression;
    }

    @Override
    public Command interpret(ExpressionContext context) {
        return context.resolve(
                expression.interpret(context),
                context.getMessage(),
                context.getAnswerConsumer());
    }
}
