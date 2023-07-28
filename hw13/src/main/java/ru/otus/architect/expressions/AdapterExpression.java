package ru.otus.architect.expressions;

public class AdapterExpression<T> implements Expression<T> {

    private final Expression<Object> objectExpression;
    private final Class<T> clazz;

    public AdapterExpression(
            Class<T> clazz,
            Expression<Object> objectExpression) {
        this.objectExpression = objectExpression;
        this.clazz = clazz;
    }

    @Override
    public T interpret(ExpressionContext context) {
        return context.resolve(
                "Adapter",
                clazz,
                objectExpression.interpret(context));
    }
}
