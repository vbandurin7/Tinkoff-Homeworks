package ru.tinkoff.edu.java.linkParser.parser;

import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;
import ru.tinkoff.edu.java.linkParser.parserResult.StackOverflowResult;
import ru.tinkoff.edu.java.linkParser.validators.StackoverflowValidator;

import java.net.URI;

public final class StackOverflowParser extends AbstactParser {
    @Override
    protected ParseResult parseImpl(URI uri) {
        final String[] splitted = uri.getSchemeSpecificPart().substring(uri.getAuthority().length() + 3).split("/");
        if (splitted.length < 2 || !splitted[0].equals("questions")) {
            throw new IllegalArgumentException("StackoverflowParser doesn't support this type of url: "
                    + uri + ". Reference on post from \"questions\" section expected");
        }
        StackoverflowValidator.validate(splitted[1]);
        return new StackOverflowResult(splitted[1]);
    }

    @Override
    protected boolean supportsImpl(String authority) {
        return authority.equals("stackoverflow.com");
    }
}
