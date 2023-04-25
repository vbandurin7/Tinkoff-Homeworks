package ru.tinkoff.edu.java.scrapper.persistence.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "url")
public class Link {

    public Link(String uri) {
        this.url = uri;
        this.lastCheckedAt = OffsetDateTime.now();
    }
    @Id
    private Long id;
    String url;
    Map<String, String> linkInfo;
    OffsetDateTime lastCheckedAt;
    OffsetDateTime updatedAt;

}
