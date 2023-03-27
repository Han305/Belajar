package com.rayhan.kantinku.exception;

public class ResetPasswordInvalidException extends Exception{
    public ResetPasswordInvalidException() {
        super();
    }

    public ResetPasswordInvalidException(String message) {
        super(message);
    }
}
