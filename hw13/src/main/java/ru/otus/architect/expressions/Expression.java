package ru.otus.architect.expressions;

public interface Expression<T> {

    T interpret(ExpressionContext context);


}
