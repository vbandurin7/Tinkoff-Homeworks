package ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepository;

import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final long checkInterval;

    private final static String DELETE_BY_URL_SQL = "DELETE FROM link WHERE url = ?";
    private final static String FIND_UNCHECKED_SQL =
        "SELECT * FROM link WHERE EXTRACT(EPOCH FROM (now() - last_checked_at)) > ?";
    private final static String FIND_BY_URL = "SELECT * FROM link WHERE url = ?";
    private final static String COUNT_SQL = "SELECT count(*) FROM link WHERE id = ?";
    private final static String COUNT_BY_URL_SQL = "SELECT count(*) FROM link WHERE url = ?";
    private final static String SAVE_SQL =
        "INSERT INTO link (url, link_info, last_checked_at, updated_at) VALUES (?, ?::jsonb, now(), ?) RETURNING id";
    private final static String UPDATE_TIME_SQL = "UPDATE link SET updated_at = ?, last_checked_at = ? WHERE url = ?";
    private static final RowMapper<LinkDto> LINK_ROW_MAPPER = (ResultSet rs, int rownum) -> {
        try {
            return new LinkDto(
                rs.getLong("id"),
                rs.getString("url"),
                new ObjectMapper().readValue(rs.getString("link_info"), HashMap.class),
                OffsetDateTime.ofInstant(rs.getTimestamp("last_checked_at").toInstant(), ZoneId.of("UTC")),
                OffsetDateTime.ofInstant(rs.getTimestamp("updated_at").toInstant(), ZoneId.of("UTC"))
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    };

    @Override
    public void deleteByUrl(String url) {
        jdbcTemplate.update(DELETE_BY_URL_SQL, url);
    }

    @Override
    public void updateTime(LinkDto linkDto) {
        jdbcTemplate.update(UPDATE_TIME_SQL, linkDto.getUpdatedAt(), linkDto.getLastCheckedAt(), linkDto.getUrl());
    }

    @Override
    public LinkDto findByUrl(String url) {
        return jdbcTemplate.queryForObject(FIND_BY_URL, LINK_ROW_MAPPER, url);
    }

    @Override
    public List<LinkDto> findUncheckedLinks() {
        return jdbcTemplate.query(FIND_UNCHECKED_SQL, LINK_ROW_MAPPER, checkInterval);
    }

    @Override
    public long countById(Long id) {
        Long count = jdbcTemplate.queryForObject(COUNT_SQL, Long.class, id);
        return count == null ? 0 : count;
    }

    @Override
    public long countByUrl(String url) {
        Long count = jdbcTemplate.queryForObject(COUNT_BY_URL_SQL, Long.class, url);
        return count == null ? 0 : count;
    }

    @Override
    public LinkDto save(LinkDto entity) {
        Long id = jdbcTemplate.queryForObject(
            SAVE_SQL,
            Long.class,
            entity.getUrl(),
            new JSONObject(entity.getLinkInfo()).toString(),
            entity.getUpdatedAt()
        );
        entity.setId(id);
        return entity;
    }
}
