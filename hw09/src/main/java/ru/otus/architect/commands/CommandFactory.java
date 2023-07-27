package ru.otus.architect.commands;

import ru.otus.architect.api.AnswerConsumer;
import ru.otus.openapi.model.Message;

@FunctionalInterface
public interface CommandFactory {

    Command create(String name, Message message, AnswerConsumer consumer);
}
