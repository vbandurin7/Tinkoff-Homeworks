package ru.tinkoff.edu.java.scrapper.persistence.service.utils;

import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;

public class EntityConverter {
    public static Link entityToDtoLink(ru.tinkoff.edu.java.scrapper.persistence.entity.Link entityLink) {
        return new Link(entityLink.getId(), entityLink.getUrl(),
                entityLink.getLinkInfo(), entityLink.getLastCheckedAt(), entityLink.getUpdatedAt());
    }

    public static Chat entityToDtoChat(ru.tinkoff.edu.java.scrapper.persistence.entity.Chat entityChat) {
        return new Chat(entityChat.getId());
    }
}
