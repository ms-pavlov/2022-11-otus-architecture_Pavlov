package ru.otus.architect.commands;

import ru.otus.openapi.model.Message;

import java.util.Map;
import java.util.function.Function;

public interface CommandBuilderStorage extends Map<String, Function<Message, Command>> {
}
