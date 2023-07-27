package ru.otus.architect.exceptions;

public class ReadPositionException extends RuntimeException {
    public ReadPositionException() {
        super();
    }

    public ReadPositionException(String s) {
        super(s);
    }
}
