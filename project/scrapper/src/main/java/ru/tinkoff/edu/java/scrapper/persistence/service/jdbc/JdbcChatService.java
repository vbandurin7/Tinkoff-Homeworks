package ru.tinkoff.edu.java.scrapper.persistence.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Chat;
import ru.tinkoff.edu.java.scrapper.persistence.repository.ChatService;

import java.sql.ResultSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Chat> CHAT_ROW_MAPPER = (ResultSet rs, int rownum) -> new Chat(rs.getLong("id"));
    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM chat WHERE id = ?", id);
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", CHAT_ROW_MAPPER);
    }

    @Override
    public long count(Long id) {
        Long count = jdbcTemplate.queryForObject("SELECT count(*) FROM chat WHERE id = ?", Long.class, id);
        return count == null ? 0 : count;
    }

    @Override
    public Chat findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM chat WHERE id = ?", CHAT_ROW_MAPPER, id);
    }

    @Override
    public void save(Chat entity) {
        jdbcTemplate.update("INSERT INTO chat VALUES (?)", entity.getId());
    }
}
