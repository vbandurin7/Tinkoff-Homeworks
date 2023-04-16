package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;
import ru.tinkoff.edu.java.scrapper.persistence.repository.LinkService;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JdbcLinkService implements LinkService {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Link> LINK_ROW_MAPPER = (ResultSet rs, int rownum) ->
            new Link(rs.getLong("id"), URI.create(rs.getString("url")), rs.getTimestamp("updated_at"));

    @Override
    public Link deleteByUrl(String url) {
        return jdbcTemplate.queryForObject("DELETE FROM link WHERE url = ?", Link.class, url);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM link WHERE id = ?", id);
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.queryForList("SELECT * FROM link", Link.class);
    }

    @Override
    public List<Link> findAllByChat(long chatId) {
        return jdbcTemplate.query(
                "SELECT * FROM link WHERE id IN (SELECT link_id FROM chat_link WHERE chat_id = ?)", LINK_ROW_MAPPER, chatId
        );
    }

    @Override
    public Link findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM link WHERE id = ?",LINK_ROW_MAPPER, id);
    }

    @Override
    public Link findByUrl(String url) {
        return jdbcTemplate.queryForObject("SELECT * FROM link WHERE url = ?", LINK_ROW_MAPPER, url);
    }

    @Override
    public long count(Long id) {
        Long count = jdbcTemplate.queryForObject("SELECT count(*) FROM link WHERE id = ?", Long.class, id);
        return count == null ? 0 : count;
    }

    @Override
    public void save(Link entity) {
        if (count(entity.getId()) == 0) {
            Long id = jdbcTemplate.queryForObject("INSERT INTO link (url) VALUES (?) RETURNING id",
                    Long.class, entity.getUrl().toString());
            entity.setId(id);
        }
    }
}
