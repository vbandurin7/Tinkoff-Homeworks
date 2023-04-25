package ru.tinkoff.edu.java.scrapper.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "link")
@Getter
@Setter
@NoArgsConstructor
public class Link {

    public Link(String url, Set<Chat> chats, Map<String, String> linkInfo, OffsetDateTime lastCheckedAt, OffsetDateTime updatedAt) {
        this.url = url;
        this.chats = chats;
        this.linkInfo = linkInfo;
        this.lastCheckedAt = lastCheckedAt;
        this.updatedAt = updatedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "url", unique = true, nullable = false)
    private String url;
    @ManyToMany
    private Set<Chat> chats;

    @Column(name = "link_info", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> linkInfo;

    @Column(name = "last_checked_at")
    private OffsetDateTime lastCheckedAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
