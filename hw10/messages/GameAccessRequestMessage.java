package messages;

import java.util.UUID;

public record GameAccessRequestMessage(
        UUID gameId,
        String userName) {
}
