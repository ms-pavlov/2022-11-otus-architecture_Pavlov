package ru.otus.architect.securities;

import java.util.*;

public record User(
        String username,
        String password,
        Map<String, List<Long>> accesses) {

    public User(String username, String password) {
        this(username, password, new HashMap<>());
    }

    public void addAccess(String game, Long objectId) {
        if (!accesses.containsKey(game)) {
            accesses.put(game, List.of(objectId));
        }
        Optional.ofNullable(game)
                .map(accesses::get)
                .filter(list -> !list.contains(objectId))
                .ifPresent(list -> list.add(objectId));
    }

    public boolean hasAccess(String game, Long objectId) {
        return Optional.ofNullable(game)
                .map(accesses::get)
                .map(list -> list.contains(objectId))
                .orElse(false);
    }

    public List<Long> getGameAccesses(String game) {
        return Optional.ofNullable(game)
                .map(accesses::get)
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }
}
