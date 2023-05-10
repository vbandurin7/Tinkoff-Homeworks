package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import java.util.List;

public interface Bot extends UpdatesListener {

    @Override
    int process(List<Update> updates);

}
