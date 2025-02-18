package com.project.springbootjavafx.exceptions;

/**
 * The {@code WrongLetterException} class represents an exception that is thrown when
 * an input contains an invalid or unexpected letter. This exception is used to indicate
 * that a provided value does not meet the required letter format or constraints.
 *
 * <p>
 * This exception extends {@link RuntimeException} and is typically used to signal an error
 * in data validation related to letter content.
 * </p>
 *
 * @see RuntimeException
 */
public class WrongLetterException extends RuntimeException {

    /**
     * Constructs a new {@code WrongLetterException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public WrongLetterException(String message) {
        super(message);
    }
}
