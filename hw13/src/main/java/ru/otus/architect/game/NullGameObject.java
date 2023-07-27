package ru.otus.architect.game;

public class NullGameObject implements GameObject {
    @Override
    public Object getParameter(String parameterName) {
        return null;
    }

    @Override
    public void setParameter(String parameterName, Object value) {
        throw new RuntimeException();
    }
}
