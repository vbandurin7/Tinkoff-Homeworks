package ru.tinkoff.edu.java.link_parser.parser;

import ru.tinkoff.edu.java.link_parser.parserResult.ParseResult;

import java.net.URI;
import java.net.URISyntaxException;

public abstract sealed class AbstactParser implements Parser permits GitHubParser, StackOverflowParser {
    public ParseResult parse(URI url) {
        if (supports(url.getAuthority())) {
            return parseImpl(url);
        }
        return null;
    }

    protected URI initURI(URI url) {
        try {
            return new URI(url.toString());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("wrong format of url " + url + ", unable to parse: " + e.getMessage());
        }
    }

    public abstract boolean supports(String authority);

    protected abstract ParseResult parseImpl(URI uri);
}
