package ru.tinkoff.edu.java.bot.bot.command.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.validator.CommandValidator;

import static ru.tinkoff.edu.java.bot.bot.command.commands.Command.START;

@Component
@RequiredArgsConstructor
public final class StartProcessor extends AbstractCommandProcessor {

    private final CommandValidator commandValidator;

    @Override
    public String command() {
        return START.getCommand();
    }

    @Override
    public String description() {
        return START.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        // if user.registered -> return "You are already registered" (вроде как мы не можем это сейчас обработать, пока нет бд)
        if (commandValidator.validateCommand( 0, update.message().text()).isEmpty()) {
            return send(update, "No arguments expected");
        }
        return send(update, "You have successfully registered");
    }

}