package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChannelActivity_One extends AppCompatActivity {

    FirebaseAuth Fdb;
    DatabaseReference FdbRef,chatnode;
    private TextView displayText,sendText;
    private String displaystring,sendstring,username,FinalMsgtoDB,FinalMsgfromDB;
    private Button sendbtn;
    private int messageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel__one);

        displayText=findViewById(R.id.display_msg_box);
        sendText=findViewById(R.id.input_text_msg);
        sendbtn=findViewById(R.id.msg_send_btn);

        //chatnode
        chatnode=FirebaseDatabase.getInstance().getReference().child("messages").child("group message channel 1");
        chatnode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                displayText.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Fdb
        Fdb=FirebaseAuth.getInstance();
        FirebaseUser user_now=FirebaseAuth.getInstance().getCurrentUser();
        String userDB_ID=user_now.getUid();
        FdbRef= FirebaseDatabase.getInstance().getReference().child("users").child(userDB_ID);
        //fetch username from database
        FdbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username=dataSnapshot.getValue().toString();
                //displayChatMessages();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatnode=FirebaseDatabase.getInstance().getReference().child("messages").child("group message channel 1").push();
                //fetch msg from txtbox
                sendstring=sendText.getText().toString();
                FinalMsgtoDB=username+": "+sendstring;
                System.out.println(FinalMsgtoDB);

                chatnode.setValue(FinalMsgtoDB).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            System.out.println("Succsessfully saved to database. ");
                            sendText.setText("");
                            //displayChatMessages();
                        }
                    }
                });

            }
        });
    }

    void displayChatMessages()
    {
        chatnode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageCount=(int)dataSnapshot.getChildrenCount();
                if(messageCount>50)
                {
                    //FdbRef.removeValue();
                }
                FinalMsgfromDB=(String)dataSnapshot.getValue();
                displayText.setText("\n"+FinalMsgfromDB);
                System.out.println("Fetch message successful");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Fetch messaeg failed.");
            }
        });
    }
}
