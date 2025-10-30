package com.example.demo.exception;

public class CardAlreadyExist extends RuntimeException {
    public CardAlreadyExist(Long number) {

        super("Card with number " + number + " already exist");
    }
}
