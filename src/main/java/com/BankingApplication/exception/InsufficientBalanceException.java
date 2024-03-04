package com.BankingApplication.exception;

public class InsufficientBalanceException extends Throwable {
    public InsufficientBalanceException(String s) {
        super(s);
    }
}
