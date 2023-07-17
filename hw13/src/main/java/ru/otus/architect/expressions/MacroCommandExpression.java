package ru.otus.architect.expressions;

import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.macrocommands.TransactionalMacroCommandChange;

import java.util.List;

public class MacroCommandExpression implements Expression<Command> {

    private final List<Expression<Command>> expressions;

    public MacroCommandExpression(Expression<Command>... expressions) {
        this.expressions = List.of(expressions);
    }

    @Override
    public Command interpret(ExpressionContext context) {
        return new TransactionalMacroCommandChange(
                expressions.stream()
                        .map(commandExpression -> commandExpression.interpret(context))
                        .toList());
    }
}
