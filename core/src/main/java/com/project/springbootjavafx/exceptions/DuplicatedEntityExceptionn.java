package com.project.springbootjavafx.exceptions;

/**
 * The {@code DuplicatedEntityExceptionn} class represents an exception thrown when an attempt is made to create or
 * add an entity that already exists in the system.
 *
 * <p>
 * This exception extends {@link RuntimeException} and is typically used to signal a violation of uniqueness constraints
 * in the application.
 * </p>
 *
 * @see RuntimeException
 */
public class DuplicatedEntityExceptionn extends RuntimeException {

    /**
     * Constructs a new {@code DuplicatedEntityExceptionn} with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public DuplicatedEntityExceptionn(String message) {
        super(message);
    }
}
