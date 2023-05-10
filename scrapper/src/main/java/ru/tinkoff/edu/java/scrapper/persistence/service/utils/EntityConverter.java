package ru.tinkoff.edu.java.scrapper.persistence.service.utils;

import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;

public final class EntityConverter {

    private EntityConverter() {
    }

    public static LinkDto entityToDtoLink(ru.tinkoff.edu.java.scrapper.persistence.entity.Link entityLink) {
        return new LinkDto(entityLink.getId(), entityLink.getUrl(),
                entityLink.getLinkInfo(), entityLink.getLastCheckedAt(), entityLink.getUpdatedAt());
    }

    public static ChatDto entityToDtoChat(ru.tinkoff.edu.java.scrapper.persistence.entity.Chat entityChat) {
        return new ChatDto(entityChat.getId());
    }
}
