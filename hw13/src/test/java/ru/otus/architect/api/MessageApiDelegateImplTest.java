package ru.otus.architect.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.CommandFactory;
import ru.otus.openapi.api.MessageApiDelegate;
import ru.otus.openapi.model.Message;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageApiDelegateImplTest {

    private final static UUID GAME_UUID = UUID.randomUUID();
    private final static Map<String, String> RESULT_STRING = Map.of("key", "test");


    @Mock
    private BiConsumer<String, Command> commandConsumerStrategy;
    @Mock
    private CommandFactory commandFactory;
    @Mock
    private Message message;
    @Mock
    private ServerWebExchange exchange;
    @Mock
    private Command command;


    private MessageApiDelegate messageApiDelegate;

    @BeforeEach
    void setUp() {
        messageApiDelegate = new MessageApiDelegateImpl(commandConsumerStrategy, commandFactory);
    }

    @Test
    void processMessages() {
        when(message.getGame()).thenReturn(GAME_UUID);
        when(commandFactory.create(any(), any(), any())).thenAnswer(
                invocationOnMock -> {
                    invocationOnMock.getArgument(2, AnswerConsumer.class).accept(RESULT_STRING);
                    return command;
                });

        var result = messageApiDelegate.processMessages(Mono.just(message), exchange)
                .block();

        verify(commandConsumerStrategy, times(1)).accept(eq(GAME_UUID.toString()), eq(command));
        verify(commandFactory, times(1)).create(eq("Message.InterpretCommand"), eq(message), any());
    }
}