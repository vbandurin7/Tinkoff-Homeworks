package ru.tinkoff.edu.java.scrapper.persistence.repository;

import ru.tinkoff.edu.java.scrapper.persistence.dto.Chat;

public interface ChatRepository extends CrudRepository<Chat, Long> {

    Chat findById(Long id);

    void deleteById(Long id);
}
