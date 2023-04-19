package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.ChatRepository;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dslContext;

    @Override
    public void deleteById(Long id) {
        dslContext.deleteFrom(CHAT).where(CHAT.ID.eq(id)).execute();
    }

    @Override
    public List<Chat> findAll() {
        return dslContext.select(CHAT.fields()).from(CHAT).fetchInto(Chat.class);
    }

    @Override
    public Chat findById(Long id) {
        var res = dslContext.select(CHAT.fields()).from(CHAT).
                where(CHAT.ID.eq(id)).limit(1).fetchInto(Chat.class);
        return res.size() == 0 ? null : res.get(0);
    }

    @Override
    public Chat save(Chat entity) {
        dslContext.insertInto(CHAT, CHAT.ID).values(entity.getId()).execute();
        return entity;
    }

    @Override
    public long countById(Long aLong) {
        return dslContext.selectCount().from(CHAT).where(CHAT.ID.eq(aLong)).fetchOne().value1();
    }
}
