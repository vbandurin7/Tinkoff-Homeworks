package ru.tinkoff.edu.java.scrapper.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "chat")
@Getter
@Setter
@NoArgsConstructor
public class Chat {

    public Chat(long id) {
        this.id = id;
    }
    @Id
    private long id;

    @ManyToMany(mappedBy = "chats")
    private Set<Link> links;
}
