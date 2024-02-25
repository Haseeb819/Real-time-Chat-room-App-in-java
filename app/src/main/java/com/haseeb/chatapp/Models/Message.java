package com.haseeb.chatapp.Models;

public class Message {
    String message,key,name;
    public  Message(){}

    public Message(String message, String name) {
        this.message = message;
        this.key = key;
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return "Message ("+
                "message='"+ message + '\''+
                ", name='" + name + '\''+
                ", key='" + key + '\''+
                ')';
    }
}
