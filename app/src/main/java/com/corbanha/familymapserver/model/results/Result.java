package com.corbanha.familymapserver.model.results;

public abstract class Result {
    private String message;

    /**
     * A basic Result, must be implemented as a subclass to Result
     * @param message
     */
    public Result(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}