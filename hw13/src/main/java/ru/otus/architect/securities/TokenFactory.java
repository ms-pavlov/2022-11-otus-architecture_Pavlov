package ru.otus.architect.securities;

import java.util.UUID;

public interface TokenFactory {

    String create(UUID uuid, User user);

}
