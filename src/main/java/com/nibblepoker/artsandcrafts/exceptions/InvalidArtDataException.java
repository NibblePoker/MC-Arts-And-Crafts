package com.nibblepoker.artsandcrafts.exceptions;

/**
 * Should be thrown when a ".art" file has malformed data.
 */
public class InvalidArtDataException extends Exception {
    public InvalidArtDataException() {}

    public InvalidArtDataException(String message) {
        super(message);
    }
}
