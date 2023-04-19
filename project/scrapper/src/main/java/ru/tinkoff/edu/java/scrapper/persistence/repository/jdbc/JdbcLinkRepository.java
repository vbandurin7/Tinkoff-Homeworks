package ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkRepository;

import java.net.URI;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final long checkInterval;
    private final static String DELETE_BY_URL_SQL = "DELETE FROM link WHERE url = ?";
    private final static String DELETE_BY_ID_SQL = "DELETE FROM link WHERE id = ?";
    private final static String FIND_ALL_SQL = "SELECT * FROM link";
    private final static String FIND_BY_ID = "SELECT * FROM link WHERE id = ?";
    private final static String FIND_BY_URL = "SELECT * FROM link WHERE url = ?";
    private static final String FIND_UNCHECKED_LINKS_SQL = "SELECT * FROM link WHERE now() - last_checked_at >= ?";
    private final static String COUNT_SQL = "SELECT count(*) FROM link WHERE id = ?";
    private final static String COUNT_BY_URL_SQL = "SELECT count(*) FROM link WHERE url = ?";
    private final static String SAVE_SQL = "INSERT INTO link (url, link_info) VALUES (?, ?::jsonb) RETURNING id";
    private final static String UPDATE_TIME_SQL = "UPDATE link SET updated_at = ?, last_checked_at = ? WHERE url = ?";
    private static final RowMapper<Link> LINK_ROW_MAPPER = (ResultSet rs, int rownum) ->
    {
        try {
            return new Link(rs.getLong("id"),
                    URI.create(rs.getString("url")),
                    new ObjectMapper().readValue(rs.getString("link_info"), HashMap.class),
                    rs.getTimestamp("last_checked_at"),
                    rs.getTimestamp("updated_at"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    };

    @Override
    public void deleteByUrl(String url) {
        jdbcTemplate.update(DELETE_BY_URL_SQL, url);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, LINK_ROW_MAPPER);
    }

    @Override
    public void updateTime(Link link) {
        jdbcTemplate.update(UPDATE_TIME_SQL, link.getUpdatedAt(), link.getLastCheckedAt(), link.getUrl().toString());
    }

    @Override
    public Link findById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID, LINK_ROW_MAPPER, id);
    }

    @Override
    public Link findByUrl(String url) {
        return jdbcTemplate.queryForObject(FIND_BY_URL, LINK_ROW_MAPPER, url);
    }

    @Override
    public List<Link> findUncheckedLinks() {
        return jdbcTemplate.query(FIND_UNCHECKED_LINKS_SQL, LINK_ROW_MAPPER, checkInterval);
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
    public Link save(Link entity) {
        Long id = jdbcTemplate.queryForObject(SAVE_SQL,
                Long.class, entity.getUrl().toString(), new JSONObject(entity.getLinkInfo()).toString());
        entity.setId(id);
        return entity;
    }

}
