package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CurrentBuild extends AppCompatActivity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_build);
        tv=findViewById(R.id.about);
        tv.setText("Current Build: 1.0.0\n\n\n Features: Group Channel for chat, signing up with email and password, listed view of texts from each user\n\n\n Planned features for next version" +
                "includes implementing multiple channels, private channels and personal messages, user avatars, better UI, further polishing ~_~");
    }
}
