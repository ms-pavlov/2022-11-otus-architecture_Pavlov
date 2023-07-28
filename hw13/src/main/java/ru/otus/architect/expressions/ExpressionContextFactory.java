package ru.otus.architect.expressions;

import ru.otus.architect.api.AnswerConsumer;
import ru.otus.openapi.model.Message;

public interface ExpressionContextFactory {

    ExpressionContext create(Message message, AnswerConsumer consumer);
}
