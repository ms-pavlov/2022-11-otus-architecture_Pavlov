package messages;

import java.util.List;
import java.util.UUID;

public record GameAccessResponseMessage(
        UUID gameId,
        String userName,
        List<Long> gameObjects
) {
}
