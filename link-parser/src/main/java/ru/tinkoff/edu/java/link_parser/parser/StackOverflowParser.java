package ru.tinkoff.edu.java.link_parser.parser;

import ru.tinkoff.edu.java.link_parser.parserResult.ParseResult;
import ru.tinkoff.edu.java.link_parser.parserResult.StackOverflowResult;
import ru.tinkoff.edu.java.link_parser.validators.StackoverflowValidator;

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
    public boolean supports(String authority) {
        return authority.equals("stackoverflow.com");
    }
}
