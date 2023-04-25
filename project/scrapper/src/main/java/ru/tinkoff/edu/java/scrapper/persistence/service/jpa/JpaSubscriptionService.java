package ru.tinkoff.edu.java.scrapper.persistence.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.dto.Link;
import ru.tinkoff.edu.java.scrapper.persistence.service.SubscriptionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaSubscriptionService implements SubscriptionService {

    private final JpaLinkService linkService;
    private final JpaChatService chatService;

    @Override
    public Link addLink(long tgChatId, String url) {
        return null;
    }

    @Override
    public Link removeLink(long tgChatId, String url) {
        return null;
    }

    @Override
    public List<Chat> chatList(String url) {
        return null;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return null;
    }
}
