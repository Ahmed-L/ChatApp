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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {


    private EditText username2, password2,Realusername;
    private Button btn2;
    private FirebaseAuth Fdb;
    private DatabaseReference FdbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn2=findViewById(R.id.signin_btn);
        username2=findViewById(R.id.Username_box2);
        password2=findViewById(R.id.password_box2);
        Realusername=findViewById(R.id.RealUsernameBox);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username,pass,Realusername_string;
                Fdb=FirebaseAuth.getInstance();
                username=username2.getText().toString();
                pass=password2.getText().toString();
                Realusername_string=Realusername.getText().toString();

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

                    /*FirebaseUser user_now=FirebaseAuth.getInstance().getCurrentUser();
                    String userDB_ID=user_now.getUid();
                    FdbRef= FirebaseDatabase.getInstance().getReference().child("users").child(userDB_ID);
                    FdbRef.setValue(Realusername_string).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {

                            }
                        }
                    }); */


                    Fdb.createUserWithEmailAndPassword(username,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseUser user_now=FirebaseAuth.getInstance().getCurrentUser();
                                String userDB_ID=user_now.getUid();
                                FdbRef= FirebaseDatabase.getInstance().getReference().child("users").child(userDB_ID);
                                FdbRef.setValue(Realusername_string).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Intent signupIntent=new Intent(SignUpActivity.this, UserActivity.class);
                                            signupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(signupIntent);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(SignUpActivity.this, "error in creating account",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(SignUpActivity.this, "Could not create account.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
