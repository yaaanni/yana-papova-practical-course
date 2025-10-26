package com.example.demo.exception;

public class UserEmailNotFoundException extends RuntimeException {
    public UserEmailNotFoundException(String email) {
        super("User with email " + email + " not found");
    }
}
