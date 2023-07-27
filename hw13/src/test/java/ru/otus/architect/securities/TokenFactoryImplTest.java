package ru.otus.architect.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenFactoryImplTest {
    private final static UUID GAME_UUID = UUID.randomUUID();
    private final static List<Long> ACCESSES = List.of(1L);
    private final static User USER = new User(
            "test",
            "pass",
            Map.of(GAME_UUID.toString(), ACCESSES));
    private final static KeyService KEY_SERVICE = new KeyServiceImpl();

    private TokenFactory tokenFactory;

    @BeforeEach
    void setUp() {
        tokenFactory = new TokenFactoryImpl(KEY_SERVICE);
    }

    @Test
    @DisplayName("Создает токен подписанный закрытым ключем, который содержит имя пользователя, uuid игры и доступные к управлению в игре объекты")
    void create() {
        var result = tokenFactory.create(GAME_UUID, USER);

        assertDoesNotThrow(() -> {
            Claims body = (Claims) Jwts.parserBuilder()
                    .setSigningKey(KEY_SERVICE.getPrivate())
                    .build()
                    .parse(result).getBody();

            List<Long> accesses = ((List<Integer>) body.get("accesses")).stream()
                    .map(Long::valueOf)
                    .toList();

            assertEquals(ACCESSES, accesses);
            assertEquals(GAME_UUID.toString(), body.get("game", String.class));
            assertEquals(USER.username(), body.getSubject());
        });
    }
}