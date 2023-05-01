package ru.tinkoff.edu.java.scrapper.dto.request;

import lombok.Getter;
import lombok.Setter;
import ru.tinkoff.edu.java.scrapper.persistence.dto.LinkDto;

@Getter
@Setter
public class LinkSaveRequest {

    public LinkSaveRequest(LinkDto dtoLink, ru.tinkoff.edu.java.scrapper.persistence.entity.Link entityLink) {
        this.dtoLink = dtoLink;
        this.entityLink = entityLink;
    }
    private LinkDto dtoLink;
    private ru.tinkoff.edu.java.scrapper.persistence.entity.Link entityLink;
}
