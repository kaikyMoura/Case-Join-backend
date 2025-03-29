package com.casejoin.productInventory.exception;

public class RequiredArgumentsMissing extends Exception {
    private static final long serialVersionUID = 1L;
    
    public RequiredArgumentsMissing(String message) {
        super(message);
    }

}
