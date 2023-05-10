package ru.tinkoff.edu.java.scrapper.dto.request;

import lombok.Getter;
import lombok.Setter;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;

@Getter
@Setter
public class ChatSaveRequest {

    public ChatSaveRequest(ChatDto dtoChat, ru.tinkoff.edu.java.scrapper.persistence.entity.Chat entityChat) {
        this.dtoChat = dtoChat;
        this.entityChat = entityChat;
    }

    private ChatDto dtoChat;
    private ru.tinkoff.edu.java.scrapper.persistence.entity.Chat entityChat;
}
