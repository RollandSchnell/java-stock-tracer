package com.stock.app.exceptions;

public class RuleNotFoundException extends Exception {

    public RuleNotFoundException(String message) {
        super(message);
    }

    public RuleNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
