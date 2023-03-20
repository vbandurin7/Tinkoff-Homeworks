package ru.tinkoff.edu.java.linkParser.parser;

import ru.tinkoff.edu.java.linkParser.parserResult.GitHubResult;
import ru.tinkoff.edu.java.linkParser.validators.GitHubValidator;

import java.net.URI;

public final class GitHubParser extends AbstactParser {
    @Override
    protected GitHubResult parseImpl(URI uri) {
        final String[] splitted = uri.getSchemeSpecificPart().substring(uri.getAuthority().length() + 3).split("/");
        if (splitted.length < 2) {
            throw new IllegalArgumentException("GitHubParser doesn't support this type of url "
                    + uri + " Reference on someone's git repository expected.");
        }
        GitHubValidator.validate(splitted[0], splitted[1]);
        return new GitHubResult(splitted[0], splitted[1]);
    }

    @Override
    protected boolean supportsImpl(String authority) {
        return authority.equals("github.com");
    }

}
