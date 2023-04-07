package ru.otus.architect.commands.macrocommands;

public class IncorrectReverseCommandListException extends RuntimeException{

    public IncorrectReverseCommandListException(String message) {
        super(message);
    }
}
