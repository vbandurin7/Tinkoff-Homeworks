package ru.tinkoff.edu.java.scrapper.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;

@Getter
@Setter
public class ChatSaveRequest {

    public ChatSaveRequest(Chat dtoChat, ru.tinkoff.edu.java.scrapper.persistence.entity.Chat entityChat) {
        this.dtoChat = dtoChat;
        this.entityChat = entityChat;
    }
    private Chat dtoChat;
    private ru.tinkoff.edu.java.scrapper.persistence.entity.Chat entityChat;
}
