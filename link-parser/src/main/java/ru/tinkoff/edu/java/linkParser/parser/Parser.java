package ru.tinkoff.edu.java.linkParser.parser;

import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;

import java.net.URI;
import java.net.URL;

public sealed interface Parser permits AbstactParser {
    ParseResult parse(URI url);

    boolean supports(String authority);
}
