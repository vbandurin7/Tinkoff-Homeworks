package ru.tinkoff.edu.java.scrapper.persistence.service.rabbitmq;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

public interface UpdateSender {

    void postUpdate(LinkUpdateRequest update);

}
