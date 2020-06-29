package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText username,password;
    TextView registerText;
    Button btn;
    FirebaseAuth Fdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this,"Successfully connected to the server.", Toast.LENGTH_LONG).show();

        username=findViewById(R.id.Username_box2);
        password=findViewById(R.id.password_box2);
        btn=findViewById(R.id.signin_btn);
        TextView signUpText=findViewById(R.id.signup_text);
        String signUpTextClick="Don't have an account? Sign up now!";
        SpannableString signUpSpan= new SpannableString(signUpTextClick);
        ClickableSpan clickSignUp = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        };
        signUpSpan.setSpan(clickSignUp,23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user,pass;
                user=username.getText().toString();
                pass=password.getText().toString();
                Fdb=FirebaseAuth.getInstance();
                if((!user.isEmpty()&&(!pass.isEmpty())))
                {
                    Fdb.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                startActivity(new Intent(MainActivity.this,UserActivity.class));
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Error in signing in...", Toast.LENGTH_LONG).show();
                                username.requestFocus();
                                password.requestFocus();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Incorrect username or password.", Toast.LENGTH_LONG).show();
                }
            }
        });

       signUpText.setText(signUpSpan);
       signUpText.setMovementMethod(LinkMovementMethod.getInstance());


    }


}

