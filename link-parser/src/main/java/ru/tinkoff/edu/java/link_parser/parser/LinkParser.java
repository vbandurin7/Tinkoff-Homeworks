package ru.tinkoff.edu.java.link_parser.parser;

import ru.tinkoff.edu.java.link_parser.parser.parserLinker.LinkParserChain;
import ru.tinkoff.edu.java.link_parser.parserResult.ParseResult;

import java.net.URI;

public final class LinkParser {

    private LinkParser() {
    }

    public static ParseResult parseURL(URI url) {
        LinkParserChain linkParserChain = new LinkParserChain();
        String authority = url.getAuthority();
        for (Parser parser : linkParserChain.getParserList()) {
            if (parser.supports(authority)) {
                return parser.parse(url);
            }
        }
        return null;
    }
}

