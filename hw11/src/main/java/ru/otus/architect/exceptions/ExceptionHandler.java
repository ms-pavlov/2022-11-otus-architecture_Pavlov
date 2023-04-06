package ru.otus.architect.exceptions;

import ru.otus.architect.commands.Command;

public interface ExceptionHandler {
    void handle(Command cmd, Exception ex);
}
