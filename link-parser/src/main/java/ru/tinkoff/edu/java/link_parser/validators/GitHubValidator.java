package ru.tinkoff.edu.java.link_parser.validators;

public final class GitHubValidator implements Validator {
    private GitHubValidator() {
    }

    private static final String USERNAME_REGEXP = "^[a-zA-Z][a-zA-Z0-9_-]{1,39}$";
    private static final String REPOSITORY_REGEXP = "^[a-zA-Z][a-zA-Z0-9_-]{1,100}$";

    public static void validate(String username, String repository) {
        if (!username.matches(USERNAME_REGEXP)) {
            throw new IllegalArgumentException("""
                    Username is incorrect, latin letters, digits, and "-" and "_" \
                    with length from 1 to 39 expected, got \
                    """ + username);
        }
        if (!repository.matches(REPOSITORY_REGEXP)) {
            throw new IllegalArgumentException("""
                    Repository name is incorrect, latin letters, digits, "-" and "_" \
                    with length from 1 to 100 expected, got \
                    """ + repository);
        }
    }
}
