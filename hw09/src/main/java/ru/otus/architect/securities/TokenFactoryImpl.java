package ru.otus.architect.securities;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenFactoryImpl implements TokenFactory {

    private final KeyService keyService;


    public TokenFactoryImpl(KeyService keyService) {
        this.keyService = keyService;
    }

    @Override
    public String create(UUID uuid, User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.username())
                .setExpiration(accessExpiration)
                .signWith(keyService.getPrivate())
                .claim("accesses", user.getGameAccesses(uuid.toString()))
                .claim("game", uuid.toString())
                .compact();
    }
}
