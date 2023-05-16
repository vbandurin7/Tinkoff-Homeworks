package ru.tinkoff.edu.java.scrapper.persistence.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.persistence.service.rabbitmq.UpdateSender;

@Service
@RequiredArgsConstructor
public class UpdatePoster {

    private final UpdateSender updateSender;

    public void sendMessage(LinkUpdateRequest linkUpdateRequest) {
        updateSender.sendUpdate(linkUpdateRequest);
    }
}
