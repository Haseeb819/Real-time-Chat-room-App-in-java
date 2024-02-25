package com.haseeb.chatapp.Models;

public class Users
{
    String uid,email,name;

    public Users(){}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return "User("+
                "uid='"+ uid + '\''+
                ", name='" + name + '\''+
                ", email='" + email + '\''+
                ')';
    }
}
