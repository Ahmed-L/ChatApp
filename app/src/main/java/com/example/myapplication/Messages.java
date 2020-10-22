package com.example.myapplication;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Messages {
    private String username;
    private String textMessage;
    private String date;


    Messages()
    {

    }

    Messages(String username, String textMessage)
    {
        this.username=username;
        this.textMessage=textMessage;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        date= sdf.format(new Date());
    }


    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
