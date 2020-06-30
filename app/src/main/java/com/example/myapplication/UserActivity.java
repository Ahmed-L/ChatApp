package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {

    private  FirebaseAuth Fdb;
    private FirebaseUser currentUser;
    private DatabaseReference FdbRef;
    private Button logout_btn,channelbtn_1;
    private TextView welcome_box;
    //private TextView current_build;
    //private String welcome_string;
    //private RecyclerView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        String currentUID=currentUser.getUid();
        FdbRef=FirebaseDatabase.getInstance().getReference().child("users").child(currentUID);
        logout_btn=findViewById(R.id.logout_button);
        channelbtn_1=findViewById(R.id.groupchat_btn_1);
        welcome_box=findViewById(R.id.welcome_msg);
        //userList=(RecyclerView)findViewById(R.id.RecyclerView_one);
        //userList.hasFixedSize();
        //userList.setLayoutManager(new LinearLayoutManager(this));

        TextView current_build=findViewById(R.id.current_build_textview);
        String currentBuild="See current build";
        SpannableString buildSpan= new SpannableString(currentBuild);
        ClickableSpan clickable = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(UserActivity.this, CurrentBuild.class));
            }
        };
        buildSpan.setSpan(clickable,4, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        current_build.setText(buildSpan);
        current_build.setMovementMethod(LinkMovementMethod.getInstance());

        FdbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.getValue().toString();
                String disp="Welcome, "+name;
                welcome_box.setText(disp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(currentUser==null)
        {
            gotoMainMenu();
        }

        //Recycler View


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMainMenu();
            }
        });
        channelbtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Channel1=new Intent(UserActivity.this, ChannelActivity_One.class);
                startActivity(Channel1);
                //finish();
            }
        });
    }

    public void gotoMainMenu()
    {
        startActivity(new Intent(UserActivity.this, MainActivity.class));
        finish();
    }

}

