package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.SubscriptionRepository;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class JooqSubscriptionRepository implements SubscriptionRepository {

    private final DSLContext dslContext;

    @Override
    public void addRelation(long tgChatId, String linkUrl) {
        dslContext.insertInto(CHAT_LINK)
                .set(CHAT_LINK.CHAT_ID, tgChatId)
                .set(CHAT_LINK.LINK_URL, linkUrl)
                .execute();
    }

    @Override
    public void deleteRelation(long tgChatId, String linkUrl) {
        dslContext.deleteFrom(CHAT_LINK)
                .where(CHAT_LINK.CHAT_ID.eq(tgChatId))
                .and(CHAT_LINK.LINK_URL.eq(linkUrl))
                .execute();
    }

    @Override
    public List<Link> findAllByChat(long tgChatId) {
        return dslContext.select()
                .from(LINK)
                .where(LINK.URL.in(
                        dslContext.select(LINK.URL)
                                .from(CHAT_LINK)
                                .where(CHAT_LINK.CHAT_ID.eq(tgChatId)).fetch())).fetchInto(Link.class);
    }

    @Override
    public List<Chat> findChatsByLink(String url) {
        return dslContext.select()
                .from(CHAT)
                .where(CHAT.ID.in(
                        dslContext.select(CHAT.ID)
                                .from(CHAT_LINK)
                                .where(CHAT_LINK.LINK_URL.eq(url)).fetch())).fetchInto(Chat.class);
    }

    @Override
    public long countLinkTracks(String linkUrl) {
        return dslContext.selectCount().from(CHAT_LINK).where(CHAT_LINK.LINK_URL.eq(linkUrl)).fetchOne().value1();
    }

    @Override
    public long countChatTracks(Long id) {
        return dslContext.selectCount().from(CHAT_LINK).where(CHAT_LINK.CHAT_ID.eq(id)).fetchOne().value1();
    }
}
