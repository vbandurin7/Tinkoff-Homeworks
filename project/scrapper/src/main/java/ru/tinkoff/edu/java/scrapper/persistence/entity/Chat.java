package ru.tinkoff.edu.java.scrapper.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Chat {
    @Id
    private long id;

}
