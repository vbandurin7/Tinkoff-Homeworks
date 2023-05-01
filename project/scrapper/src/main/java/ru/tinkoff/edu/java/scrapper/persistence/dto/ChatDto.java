package ru.tinkoff.edu.java.scrapper.persistence.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ChatDto {
    @Id
    private long id;
}
