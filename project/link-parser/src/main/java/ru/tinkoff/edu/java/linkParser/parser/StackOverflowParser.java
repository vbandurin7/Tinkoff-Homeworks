package ru.tinkoff.edu.java.linkParser.parser;

import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;
import ru.tinkoff.edu.java.linkParser.parserResult.StackOverflowResult;
import ru.tinkoff.edu.java.linkParser.validators.StackoverflowValidator;

import java.net.URI;


public final class StackOverflowParser extends AbstactParser {
    @Override
    protected ParseResult parseImpl(URI uri) {
        StackoverflowValidator stackoverflowValidator = new StackoverflowValidator();
        final String[] splitted = uri.getSchemeSpecificPart().substring(uri.getAuthority().length() + 3).split("/");
        if (splitted.length >= 2 && splitted[0].equals("questions")) {
            if (stackoverflowValidator.checkIfValid(splitted[1])) {
                return new StackOverflowResult(splitted[1]);
            }
            throw new IllegalArgumentException("ID is incorrect, integer expected, got " + splitted[2]);
        }
        throw new IllegalArgumentException("StackoverflowParser doesn't support this type of url: "
                + uri + ". Reference on post from \"questions\" section expected");
    }

    @Override
    public boolean supports(String authority) {
        return authority.equals("stackoverflow.com");
    }
}
