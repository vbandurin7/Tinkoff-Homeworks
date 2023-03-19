package ru.tinkoff.edu.java.linkParser.parser.parserLinker;

import ru.tinkoff.edu.java.linkParser.parser.AbstactParser;
import ru.tinkoff.edu.java.linkParser.parser.GitHubParser;
import ru.tinkoff.edu.java.linkParser.parser.StackOverflowParser;

import java.util.ArrayList;
import java.util.List;

public final class LinkParserChain {
    private final List<? extends AbstactParser> parserList = new ArrayList<>(
            List.of(
                    new GitHubParser(),
                    new StackOverflowParser()
            )
    );
    public List<? extends AbstactParser> getParserList() {
        return parserList;
    }
}
