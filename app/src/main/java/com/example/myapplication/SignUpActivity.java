package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {


    private EditText username2, password2;
    private Button btn2;
    FirebaseAuth Fdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn2=findViewById(R.id.signin_btn);
        username2=findViewById(R.id.Username_box2);
        password2=findViewById(R.id.password_box2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,pass;
                Fdb=FirebaseAuth.getInstance();
                username=username2.getText().toString();
                pass=password2.getText().toString();
                if(username.isEmpty() || pass.isEmpty())
                {
                    Toast.makeText(SignUpActivity.this,"email or password is empty",Toast.LENGTH_LONG).show();
                    if(username.isEmpty())
                    {
                        username2.requestFocus();
                    }
                    else
                    {
                        password2.requestFocus();
                    }
                }
                else
                {
                    Fdb.createUserWithEmailAndPassword(username,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                startActivity(new Intent(SignUpActivity.this,UserActivity.class));
                            }
                            else
                            {
                                Toast.makeText(SignUpActivity.this, "error in creating account",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
