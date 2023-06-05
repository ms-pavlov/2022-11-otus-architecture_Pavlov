package ru.otus.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.openapi.api.MessageApiDelegate;
import ru.otus.openapi.model.Message;
import ru.otus.openapi.model.MessageResponse;

@Service
public class MessageApiDelegateImpl implements MessageApiDelegate {

    @Override
    public ResponseEntity<MessageResponse> processMessages(Message message) {
        throw new RuntimeException("Not implemented yet!");
    }
}
