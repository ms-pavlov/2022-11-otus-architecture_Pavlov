package ru.otus.architect.game;

public interface GameObject {

    Object getParameter(String parameterName);

    void setParameter(String parameterName, Object value);
}
