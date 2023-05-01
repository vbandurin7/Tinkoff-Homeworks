package ru.tinkoff.edu.java.scrapper.persistence.repository;

import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;

public interface ChatRepository extends CrudRepository<ChatDto, Long> {

    ChatDto findById(Long id);

    void deleteById(Long id);
}
