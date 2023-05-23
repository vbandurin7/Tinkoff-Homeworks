package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.repository.SubscriptionRepository;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.*;

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
    public List<LinkDto> findAllByChat(long tgChatId) {
        return dslContext.select()
            .from(LINK)
            .where(LINK.URL.in(
                dslContext.select(CHAT_LINK.LINK_URL)
                    .from(CHAT_LINK)
                    .where(CHAT_LINK.CHAT_ID.eq(tgChatId)).fetch()))
            .fetchInto(LinkDto.class);
    }

    @Override
    public List<ChatDto> findChatsByLink(String url) {
        return dslContext.select()
            .from(CHAT)
            .where(CHAT.ID.in(
                dslContext.select(CHAT_LINK.CHAT_ID)
                    .from(CHAT_LINK)
                    .where(CHAT_LINK.LINK_URL.eq(url)).fetch())).fetchInto(ChatDto.class);
    }

    @Override
    public long countLinkTracks(String linkUrl) {
        return dslContext.selectCount().from(CHAT_LINK).where(CHAT_LINK.LINK_URL.eq(linkUrl)).fetchOne().value1();
    }

    @Override
    public long countChatTracks(Long id) {
        return dslContext.selectCount().from(CHAT_LINK).where(CHAT_LINK.CHAT_ID.eq(id)).fetchOne().value1();
    }

    public void deleteAll() {
        dslContext.deleteFrom(CHAT_LINK).execute();
    }
}
