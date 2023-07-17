package ru.otus.architect.expressions;

import ru.otus.architect.api.AnswerConsumer;
import ru.otus.openapi.model.Message;

public interface ExpressionContext {

    <T> T resolve(String name, Object... args);

    Message getMessage();

    AnswerConsumer getAnswerConsumer();

}
