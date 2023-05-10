package ru.tinkoff.edu.java.link_parser.parser.parserLinker;

import ru.tinkoff.edu.java.link_parser.parser.GitHubParser;
import ru.tinkoff.edu.java.link_parser.parser.Parser;
import ru.tinkoff.edu.java.link_parser.parser.StackOverflowParser;

import java.util.List;

public final class LinkParserChain {
    private final List<Parser> parserList = List.of(
            new GitHubParser(),
            new StackOverflowParser()
    );

    public List<Parser> getParserList() {
        return parserList;
    }
}
