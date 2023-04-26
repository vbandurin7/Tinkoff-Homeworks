package ru.tinkoff.edu.java.scrapper.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "url", unique = true, nullable = false)
    private String url;
    @ManyToMany(mappedBy = "links")
    private Set<Chat> chats = new HashSet<>();

    @Column(name = "link_info", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> linkInfo;

    @Column(name = "last_checked_at")
    private OffsetDateTime lastCheckedAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link course = (Link) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
