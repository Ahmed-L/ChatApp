package com.example.myapplication;

import com.google.firebase.firestore.auth.User;

public class Users {
    private String name;
    private String status;
    private String image;

    Users()
    {

    }

    Users(String a, String b, String c)
    {
        name=a;
        status=b;
        image=c;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }

    public void setStatus(String status)
    {
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
