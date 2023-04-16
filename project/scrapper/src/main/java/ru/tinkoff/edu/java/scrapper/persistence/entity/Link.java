package ru.tinkoff.edu.java.scrapper.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.net.URI;
import java.sql.Timestamp;

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
    Timestamp updated_at;
}
