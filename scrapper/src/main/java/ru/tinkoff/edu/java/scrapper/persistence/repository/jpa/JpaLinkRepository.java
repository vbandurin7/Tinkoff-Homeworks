package ru.tinkoff.edu.java.scrapper.persistence.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.persistence.entity.Link;

import java.time.OffsetDateTime;
import java.util.List;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    Link findByUrl(String url);

    @Transactional
    void deleteByUrl(String url);

    void deleteById(long id);

    List<Link> findAllByLastCheckedAtIsLessThan(OffsetDateTime time);

    long countByUrl(String url);

    @Modifying
    @Transactional
    @Query(value = "UPDATE link SET last_checked_at = ?1, updated_at = ?2 WHERE url = ?3", nativeQuery = true)
    void updateTime(OffsetDateTime lastCheckedAt, OffsetDateTime updatedAt, String url);

    @Query(value = "SELECT * FROM link WHERE EXTRACT(EPOCH FROM (now() - last_checked_at)) > ?1", nativeQuery = true)
    List<Link> findUnchecked(long checkInterval);
}
