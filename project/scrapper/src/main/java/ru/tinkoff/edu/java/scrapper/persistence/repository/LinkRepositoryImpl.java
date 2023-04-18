package ru.tinkoff.edu.java.scrapper.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.net.URI;
import java.sql.ResultSet;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LinkRepositoryImpl implements LinkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final static String DELETE_BY_URL_SQL = "DELETE FROM link WHERE url = ?";
    private final static String DELETE_BY_ID_SQL = "DELETE FROM link WHERE id = ?";
    private final static String FIND_ALL_SQL = "SELECT * FROM link";
    private final static String FIND_BY_ID = "SELECT * FROM link WHERE id = ?";
    private final static String FIND_BY_URL = "SELECT * FROM link WHERE url = ?";
    private final static String COUNT_SQL = "SELECT count(*) FROM link WHERE id = ?";
    private final static String COUNT_BY_URL_SQL = "SELECT count(*) FROM link WHERE url = ?";
    private final static String SAVE_SQL = "INSERT INTO link (url) VALUES (?) RETURNING id";
    private final static String UPDATE_TIME_SQL = "UPDATE link SET updated_at = ? WHERE url = ?";
    private static final RowMapper<Link> LINK_ROW_MAPPER = (ResultSet rs, int rownum) ->
            new Link(rs.getLong("id"), URI.create(rs.getString("url")),
                    rs.getTimestamp("last_checked_at"), rs.getTimestamp("updated_at"));

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
        jdbcTemplate.update(UPDATE_TIME_SQL, link.getUpdatedAt(), link.getUrl());
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
    public long count(Long id) {
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
                Long.class, entity.getUrl().toString());
        entity.setId(id);
        return entity;
    }
}
