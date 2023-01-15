package com.example.cube.Exception;

import org.springframework.http.HttpStatus;

public class CubeAPIException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public CubeAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
