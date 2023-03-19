package ru.tinkoff.edu.java.linkParser.validators;
public class GitHubValidator {
    public static final String USERNAME_REGEXP = "^[a-zA-Z][a-zA-Z0-9_-]{1,39}$";
    public static final String REPOSITORY_REGEXP = "^[a-zA-Z][a-zA-Z0-9_-]{1,100}$";

    public void checkIfValid(String username, String repository) {
        if (!username.matches(USERNAME_REGEXP)) {
            throw new IllegalArgumentException("Username is incorrect, latin letters, digits, and \"-\" and \"_\" " +
                    "with length from 1 to 39 expected, " +
                    "got " + username);
        }
        if (!repository.matches(REPOSITORY_REGEXP)) {
            throw new IllegalArgumentException("Repository name is incorrect, latin letters, digits, \"-\" and \"_\" " +
                    "with length from 1 to 100 expected, " +
                    "got " + repository);
        }
    }
}