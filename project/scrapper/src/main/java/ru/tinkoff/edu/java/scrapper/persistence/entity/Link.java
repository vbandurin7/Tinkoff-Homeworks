package ru.tinkoff.edu.java.scrapper.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "url")
public class Link {

    public Link(URI uri) {
        this.url = uri;
    }
    @Id
    private Long id;
    URI url;
    Map<String, String> linkInfo;
    Timestamp lastCheckedAt;
    Timestamp updatedAt;

}
