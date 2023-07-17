package ru.otus.architect.expressions;

public class GameObjectExpression implements Expression<Object> {
    @Override
    public Object interpret(ExpressionContext context) {
        return context.resolve("IoC.Game::getGameObject", context.getMessage());
    }
}
