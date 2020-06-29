package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {

    private FirebaseAuth Fdb;
    private Button logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        Fdb=FirebaseAuth.getInstance();
        FirebaseUser user_now=Fdb.getCurrentUser();
        logout_btn=findViewById(R.id.logout_button);

        if(user_now==null)
        {
            gotoMainMenu();
        }


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMainMenu();
            }
        });
    }

    public void gotoMainMenu()
    {
        startActivity(new Intent(UserActivity.this, MainActivity.class));
        finish();
    }


}

