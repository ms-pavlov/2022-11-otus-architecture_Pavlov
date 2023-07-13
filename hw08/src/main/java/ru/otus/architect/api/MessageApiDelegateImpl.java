package ru.otus.architect.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.CommandFactory;
import ru.otus.architect.game.objects.dimension.vector.Vector;
import ru.otus.architect.game.objects.dimension.vector.VectorDecorator;
import ru.otus.openapi.api.MessageApiDelegate;
import ru.otus.openapi.model.Message;
import ru.otus.openapi.model.MessageResponse;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

@Service
public class MessageApiDelegateImpl implements MessageApiDelegate {

    private final BiConsumer<String, Command> commandConsumerStrategy;
    private final CommandFactory commandFactory;

    public MessageApiDelegateImpl(
            BiConsumer<String, Command> commandConsumerStrategy,
            CommandFactory commandFactory) {
        this.commandConsumerStrategy = commandConsumerStrategy;
        this.commandFactory = commandFactory;
    }

    @Override
    public Mono<ResponseEntity<MessageResponse>> processMessages(Mono<Message> message, ServerWebExchange exchange) {
        return message.flatMap(
                value -> Mono.create(
                        monoSink -> commandConsumerStrategy.accept(
                                Optional.ofNullable(value)
                                        .map(Message::getGame)
                                        .map(UUID::toString)
                                        .orElseThrow(RuntimeException::new),
                                commandFactory.create(
                                        "Message.InterpretCommand",
                                        value,
                                        item -> monoSink.success(prepareResponse(item))))));
    }

    private ResponseEntity<MessageResponse> prepareResponse(Map<String, String> item) {
        return ResponseEntity.ok(
                new MessageResponse()
                        .status("ACCEPTED")
                        .message(item));
    }
}
