package com.testing.demo.customer.exceptions;

public class CustomerAlreadyExistException extends Exception {
    
    public CustomerAlreadyExistException(String msg) {
        super(msg);
    }
}
