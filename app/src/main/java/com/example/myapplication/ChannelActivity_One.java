package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChannelActivity_One extends AppCompatActivity {

    FirebaseAuth Fdb;
    DatabaseReference FdbRef;
    DatabaseReference chatnode;
    private TextView sendText;
    private String displaystring,sendstring,username,FinalMsgtoDB,FinalMsgfromDB;
    private Button sendbtn;
    private int messageCount;
    private ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    //private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder> mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel__one);

        listView=findViewById(R.id.list_view_msg);
        //arrayList=findViewById(R.id.list_view_msg);
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(ChannelActivity_One.this, android.R.layout.simple_list_item_1, arrayList);
        sendText=findViewById(R.id.input_text_msg);
        sendbtn=findViewById(R.id.msg_send_btn);
        listView.setAdapter(arrayAdapter);

        //chatnode
        chatnode=FirebaseDatabase.getInstance().getReference().child("messages");
        chatnode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages=dataSnapshot.getValue(Messages.class);
                String msg=messages.getTextMessage();
                String nameofUser=messages.getUsername();
                arrayList.add(nameofUser+": "+msg);
                arrayAdapter.notifyDataSetChanged();
                //displayText.setText("\n"+nameofUser+": "+msg);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayAdapter.notifyDataSetChanged();
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

                //fetch msg from txtbox
                sendstring=sendText.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("messages").push().setValue(new Messages(username,sendstring));
                sendText.setText("");


            }
        });
    }

    /*void displayChatMessages()
    {
        chatnode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               // Messages messages=dataSnapshot.getValue(Messages.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Fetch messaeg failed.");
            }
        });
    } */
}
