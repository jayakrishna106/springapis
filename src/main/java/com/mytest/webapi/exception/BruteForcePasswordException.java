package com.mytest.webapi.exception;

public class BruteForcePasswordException extends RuntimeException{
    public static String MESSAGE = "blocked";
    public BruteForcePasswordException(){
        super(MESSAGE);
    }
}
