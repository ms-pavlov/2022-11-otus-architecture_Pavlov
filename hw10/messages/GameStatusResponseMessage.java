package messages;

import java.util.UUID;

public record GameStatusResponseMessage(
        UUID gameId,
        String status
) {
}
