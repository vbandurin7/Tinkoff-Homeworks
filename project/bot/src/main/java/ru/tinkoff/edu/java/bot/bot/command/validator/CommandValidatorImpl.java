package ru.tinkoff.edu.java.bot.bot.command.validator;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public final class CommandValidatorImpl implements CommandValidator {

    @Override
    public Optional<List<String>> validateCommand(int numOfArgs, String command) {
        List<String> splCommand = List.of(command.split(" "));
        if (splCommand.size() - 1 != numOfArgs) {
            return Optional.empty();
        }
        return Optional.of(splCommand.subList(1, splCommand.size()));
    }
}
