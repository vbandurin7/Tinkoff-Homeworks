package ru.tinkoff.edu.java.linkParser.validators;

public final class StackoverflowValidator implements Validator {
    private static final String ID_REGEXP = "^[0-9]+$";

    public static void validate(String id) {
        if (!id.matches(ID_REGEXP)) {
            throw new IllegalArgumentException("ID is incorrect, digits expected, got " + id);
        }
    }
}

