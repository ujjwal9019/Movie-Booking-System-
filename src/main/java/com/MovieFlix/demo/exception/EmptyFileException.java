package com.MovieFlix.demo.exception;

public class EmptyFileException extends  RuntimeException{
    public EmptyFileException(String message){
        super(message);
    }
}
