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
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String ADD_RELATION_SQL = "INSERT into chat_link VALUES (?, ?)";
    private static final String DELETE_RELATION_SQL = "DELETE FROM chat_link WHERE chat_id = ? AND link_url = ?";
    private static final String FIND_ALL_BY_CHAT_SQL = "SELECT * FROM link WHERE url IN (SELECT link_url FROM chat_link WHERE chat_id = ?)";
    private static final String COUNT_LINK_TRACKS_SQL = "SELECT count(*) FROM chat_link WHERE link_url = ?";
    private static final String COUNT_CHAT_TRACKS_SQL = "SELECT count(*) FROM chat_link WHERE chat_id = ?";

    private static final RowMapper<Link> LINK_ROW_MAPPER = (ResultSet rs, int rownum) ->
            new Link(rs.getLong("id"), URI.create(rs.getString("url")), rs.getTimestamp("updated_at"));

    @Override
    public void addRelation(long tgChatId, String linkUrl) {
        jdbcTemplate.update(ADD_RELATION_SQL, tgChatId, linkUrl);
    }

    @Override
    public void deleteRelation(long tgChatId, String linkUrl) {
        jdbcTemplate.update(DELETE_RELATION_SQL, tgChatId, linkUrl);
    }

    @Override
    public List<Link> findAllByChat(long tgChatId) {
        return jdbcTemplate.query(
                FIND_ALL_BY_CHAT_SQL, LINK_ROW_MAPPER, tgChatId
        );
    }

    @Override
    public long countLinkTracks(String linkUrl) {
        Long count = jdbcTemplate.queryForObject(COUNT_LINK_TRACKS_SQL, Long.class, linkUrl);
        return count == null ? 0 : count;
    }

    @Override
    public long countChatTracks(Long id) {
        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_TRACKS_SQL, Long.class, id);
        return count == null ? 0 : count;
    }
}
