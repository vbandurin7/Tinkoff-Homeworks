package ru.tinkoff.edu.java.scrapper.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Link {

    public Link(URI uri) {
        this.url = uri;
    }
    @Id
    private Long id;
    URI url;
    Timestamp lastCheckedAt;
    Timestamp updatedAt;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Link ex) {
            return Objects.equals(ex.getClass(), getClass())
                    && Objects.equals(ex.getUrl(), url);
        }
        return false;
    }
}
