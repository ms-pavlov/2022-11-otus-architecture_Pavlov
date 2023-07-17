package ru.otus.architect.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class GameObjectImpl implements GameObject {

    private final Map<String, Object> parameters;

    public GameObjectImpl() {
        this.parameters = new HashMap<>();
    }

    public GameObjectImpl(Map<String, Object> parameters) {
        this.parameters = new HashMap<>(parameters);
    }

    @Override
    public Object getParameter(String parameterName) {
        return Optional.ofNullable(parameterName)
                .map(parameters::get)
                .orElse(null);
    }

    @Override
    public void setParameter(String parameterName, Object value) {
        parameters.put(parameterName, value);
    }
}
