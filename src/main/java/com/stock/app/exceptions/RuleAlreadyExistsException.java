package com.stock.app.exceptions;

public class RuleAlreadyExistsException extends Exception {

    public RuleAlreadyExistsException(String message) {
        super(message);
    }

    public RuleAlreadyExistsException(String message, Throwable t) { super(message, t); }
}
