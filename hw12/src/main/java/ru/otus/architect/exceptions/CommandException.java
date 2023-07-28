package ru.otus.architect.exceptions;

public class CommandException extends RuntimeException {

    public CommandException() {
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
