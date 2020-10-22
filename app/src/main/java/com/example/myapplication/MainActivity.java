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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText username,password;
    TextView registerText;
    Button btn;
    FirebaseAuth Fdb;
    FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener fdbListener;
    public static int userState=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this,"Connected.", Toast.LENGTH_LONG).show();

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

                /*fdbListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if(firebaseAuth.getCurrentUser()!=null)
                            startActivity(new Intent(MainActivity.this, UserActivity.class));
                    }
                }; */

                if((!user.isEmpty()&&(!pass.isEmpty())))
                {
                    Fdb.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                userState=1;
                                Intent loginIntent=new Intent(MainActivity.this, UserActivity.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(loginIntent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Incorrect Email or Password", Toast.LENGTH_LONG).show();
                                username.requestFocus();
                                password.requestFocus();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Error in signing in...", Toast.LENGTH_LONG).show();
                }
            }
        });

       signUpText.setText(signUpSpan);
       signUpText.setMovementMethod(LinkMovementMethod.getInstance());

    }

   /* @Override
    protected void onStart() {

        super.onStart();
        Fdb.addAuthStateListener(fdbListener);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        Fdb=FirebaseAuth.getInstance();
        firebaseUser=Fdb.getCurrentUser();
        if(firebaseUser!=null) {
            startActivity(new Intent(MainActivity.this, UserActivity.class));
            finish();
        }
    }
}

