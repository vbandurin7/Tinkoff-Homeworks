package ru.tinkoff.edu.java.linkParser.parser;

import ru.tinkoff.edu.java.linkParser.parser.parserLinker.LinkParserChain;
import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;

import java.net.URL;

public final class LinkParser {

    public ParseResult parseURL(URL url) {
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

