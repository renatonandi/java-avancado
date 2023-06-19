package br.com.trier.springverspertino.services.exceptions;

public class IntegrityViolation extends RuntimeException{
    
    public IntegrityViolation(String message) {
        super(message);
    }

}
