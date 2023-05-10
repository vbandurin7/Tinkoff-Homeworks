package ru.tinkoff.edu.java.link_parser.parser;

import ru.tinkoff.edu.java.link_parser.parserResult.ParseResult;

import java.net.URI;

public sealed interface Parser permits AbstactParser {
    ParseResult parse(URI url);

    boolean supports(String authority);
}
