package com.testing.demo.customer.exceptions;

public class CustomerDoesntExistException extends Exception {
    
    public CustomerDoesntExistException(String msg) {
        super(msg);
    }
}
