package ru.tinkoff.edu.java.scrapper.persistence.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepository;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.LINK;
@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {

    private final DSLContext dslContext;
    private final long checkInterval;


    @Override
    public LinkDto save(LinkDto entity) {
        final Long id = dslContext.insertInto(LINK, LINK.URL, LINK.LINK_INFO)
                .values(entity.getUrl().toString(), JSONB.jsonb(new JSONObject(entity.getLinkInfo()).toString()))
                .returning(LINK.ID).fetchOne().getId();
        entity.setId(id);
        return entity;
    }

    @Override
    public long countById(Long id) {
        return dslContext.selectCount().from(LINK).where(LINK.ID.eq(id)).fetchOne().value1();
    }

    @Override
    public LinkDto findByUrl(String url) {
        var res = dslContext.select(LINK.fields()).from(LINK).
                where(LINK.URL.eq(url)).limit(1).fetchInto(LinkDto.class);
        return res.size() == 0 ? null : res.get(0);
    }

    @Override
    public void deleteByUrl(String url) {
        dslContext.deleteFrom(LINK).where(LINK.URL.eq(url)).execute();
    }

    @Override
    public List<LinkDto> findUncheckedLinks() {
        return dslContext.select(LINK.fields()).from(LINK).fetchInto(LinkDto.class).stream().filter(link ->
                OffsetDateTime.now().toEpochSecond() - link.getLastCheckedAt().toEpochSecond() > checkInterval).toList();
    }

    @Override
    public void updateTime(LinkDto linkDto) {
        dslContext.update(LINK)
                .set(LINK.LAST_CHECKED_AT, OffsetDateTime.now())
                .set(LINK.UPDATED_AT, OffsetDateTime.ofInstant(linkDto.getUpdatedAt().toInstant(), ZoneId.of("UTC")))
                .where(LINK.URL.eq(linkDto.getUrl().toString())).execute();
    }

    @Override
    public long countByUrl(String url) {
        return dslContext.selectCount().from(LINK).where(LINK.URL.eq(url)).fetchOne().value1();
    }
}
