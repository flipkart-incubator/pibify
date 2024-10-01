package com.flipkart.pibify.validation;

/**
 * This class is used for
 * Author bageshwar.pn
 * Date 27/07/24
 */
public class InvalidPibifyAnnotation extends Exception {

    public InvalidPibifyAnnotation(String message) {
        super(message);
    }

    public static class ErrorCodes {
        public static final String MISSING_INDEX = "Missing Index";
    }
}
