package ru.tinkoff.edu.java.bot.bot.command.validator;

import ru.tinkoff.edu.java.bot.bot.command.commands.Command;

import java.util.List;
import java.util.Optional;

public sealed interface CommandValidator permits CommandValidatorImpl {

    /**
     * Checks if the number of command arguments is the same as {@code numOfArgs}.
     * It is expected that command from {@link Command} will be passed
     * as second argument.
     *
     * @param numOfArgs - number of arguments expected by the command.
     * @param command - the command passed to bot.
     * @return {@code Optional<List<String>>} which contains all arguments.
     */
    Optional<List<String>> validateCommand(int numOfArgs, String command);

}
