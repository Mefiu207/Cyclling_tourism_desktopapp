package com.project.springbootjavafx.exceptions;

public class WrongLetterException extends RuntimeException{

    public WrongLetterException(String message) {
        super(message);
    }
}
