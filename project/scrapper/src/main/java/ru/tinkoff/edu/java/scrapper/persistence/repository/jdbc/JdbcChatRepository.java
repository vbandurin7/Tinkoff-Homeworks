package ru.tinkoff.edu.java.scrapper.persistence.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.ChatRepository;

import java.sql.ResultSet;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String DELETE_BY_ID_SQL = "DELETE FROM chat WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM chat";
    private static final String COUNT_SQL = "SELECT count(*) FROM chat WHERE id = ?";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM chat WHERE id = ?";
    private static final String SAVE_SQL = "INSERT INTO chat VALUES (?)";
    private static final RowMapper<Chat> CHAT_ROW_MAPPER = (ResultSet rs, int rownum) -> new Chat(rs.getLong("id"));
    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }

    @Override
    public long countById(Long id) {
        Long count = jdbcTemplate.queryForObject(COUNT_SQL, Long.class, id);
        return count == null ? 0 : count;
    }

    @Override
    public Chat findById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, CHAT_ROW_MAPPER, id);
    }

    @Override
    public Chat save(Chat entity) {
        jdbcTemplate.update(SAVE_SQL, entity.getId());
        return entity;
    }
}
