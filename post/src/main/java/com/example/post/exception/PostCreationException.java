package com.example.post.exception;

public class PostCreationException extends RuntimeException {

    public PostCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}