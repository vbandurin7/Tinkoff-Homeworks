package ru.tinkoff.edu.java.linkParser.validators;

public class StackoverflowValidator {
    public static final String ID_REGEXP = "^[0-9]*$";

    public boolean checkIfValid(String id) {
        return id.matches(StackoverflowValidator.ID_REGEXP);
    }
}
