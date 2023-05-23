package bot.command.processor;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.bot.command.processor.ListProcessor;
import ru.tinkoff.edu.java.bot.bot.command.validator.CommandValidatorImpl;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.bot.service.LinkService;
import com.pengrad.telegrambot.model.Update;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListProcessorTest {

    private static final String LIST_COMMAND = "/list";

    @Mock
    private CommandValidatorImpl commandValidator;

    @Mock
    private LinkService linkService;

    private ListProcessor listProcessor;

    @BeforeEach
    void setup() {
        listProcessor = new ListProcessor(commandValidator, linkService);
    }

    @Test
    void command_returnsCorrectCommand() {
        assertThat(listProcessor.command()).isEqualTo(LIST_COMMAND);
    }

    @Test
    void description_descriptionIsNotEmpty() {
        assertThat(listProcessor.description()).isNotEmpty();
    }

    @SneakyThrows
    @Test
    void handle_formattingIsHTML() {
        //given
        final Update update = Mockito.mock(Update.class);
        final Message message = Mockito.mock(Message.class);
        final Chat chat = Mockito.mock(Chat.class);
        ListLinksResponse links = new ListLinksResponse(List.of(new LinkResponse(1, URI.create("test.com"))), 1);
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(555L);
        when(update.message().text()).thenReturn(LIST_COMMAND);
        when(commandValidator.validateCommand(0, update.message().text()))
            .thenReturn(Optional.of(Collections.emptyList()));
        when(linkService.getLinksList(update.message().chat().id())).thenReturn(Optional.of(links));

        //when
        final SendMessage result = listProcessor.handle(update);

        //then
        assertThat(result.getParameters().get("parse_mode")).isEqualTo(ParseMode.HTML.toString());
    }

    @SneakyThrows
    @Test
    void handle_emptyList_specialMessage() {
        //given
        final Update update = Mockito.mock(Update.class);
        final Message message = Mockito.mock(Message.class);
        final Chat chat = Mockito.mock(Chat.class);
        ListLinksResponse links = new ListLinksResponse(Collections.emptyList(), 0);
        when(update.message()).thenReturn(message);
        when(update.message().chat()).thenReturn(chat);
        when(update.message().chat().id()).thenReturn(555L);
        when(update.message().text()).thenReturn(LIST_COMMAND);
        when(commandValidator.validateCommand(0, update.message().text()))
            .thenReturn(Optional.of(Collections.emptyList()));
        when(linkService.getLinksList(update.message().chat().id())).thenReturn(Optional.of(links));

        //when
        final SendMessage result = listProcessor.handle(update);

        //then
        assertThat(result.getParameters().get("text"))
            .isEqualTo("No links for track were added. Try <b>'/track [link]'</b>");
    }
}
