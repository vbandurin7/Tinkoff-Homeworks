package ru.tinkoff.edu.java.bot.client;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.configuration.ApplicationProperties;
import ru.tinkoff.edu.java.bot.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.dto.response.DeleteChatResponse;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.bot.dto.response.RegisterChatResponse;

@EnableConfigurationProperties(ApplicationProperties.class)
@RequiredArgsConstructor
public class ScrapperClient {

    private static final String TG_CHATS_URL = "/tg-chat";
    private static final String LINKS_URL = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private final WebClient webClient;

    public ScrapperClient(String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public ResponseEntity<RegisterChatResponse> registerChat(long id) {
        return webClient.post()
                .uri(TG_CHATS_URL + "/" + id)
                .retrieve()
                .toEntity(RegisterChatResponse.class)
                .block();
    }

    public ResponseEntity<DeleteChatResponse> deleteChat(long id) {
        return webClient.delete()
                .uri(TG_CHATS_URL + "/" + id)
                .retrieve()
                .toEntity(DeleteChatResponse.class)
                .block();
    }

    public ResponseEntity<ListLinksResponse> getLinks(long tgChatId) {
        return webClient.get()
                .uri(LINKS_URL)
                .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
                .retrieve()
                .toEntity(ListLinksResponse.class)
                .block();
    }

    public ResponseEntity<LinkResponse> addLink(long tgChatId, String url) {
        return webClient.post()
                .uri(LINKS_URL)
                .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
                .bodyValue(new AddLinkRequest(url))
                .retrieve()
                .toEntity(LinkResponse.class)
                .block();
    }

    public ResponseEntity<LinkResponse> deleteLink(long tgChatId, String url) {
        return webClient.method(HttpMethod.DELETE)
                .uri(LINKS_URL)
                .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
                .bodyValue(new RemoveLinkRequest(url))
                .retrieve()
                .toEntity(LinkResponse.class)
                .block();
    }
}
