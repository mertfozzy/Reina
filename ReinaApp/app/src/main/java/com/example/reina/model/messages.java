package com.example.reina.model;

public class messages {

    private String message, type, who;

    public messages() {
    }

    public messages(String message, String type, String who) {
        this.message = message;
        this.type = type;
        this.who = who;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }


}
