package com.project.springbootjavafx.exceptions;

/**
 * The {@code WrongCodeLengthException} class represents an exception that is thrown when
 * a provided code does not meet the required length constraints.
 *
 * <p>
 * This exception extends {@link RuntimeException} and is used to indicate that an operation
 * failed because the input code is either too short or too long compared to the expected format.
 * </p>
 *
 * @see RuntimeException
 */
public class WrongCodeLengthException extends RuntimeException {

    /**
     * Constructs a new {@code WrongCodeLengthException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public WrongCodeLengthException(String message) {
        super(message);
    }
}
