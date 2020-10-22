package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChannelActivity_One extends AppCompatActivity {

    FirebaseAuth Fdb;
    DatabaseReference FdbRef;
    DatabaseReference chatnode;
    private EditText sendText;
    private String displaystring,sendstring,username,FinalMsgtoDB,FinalMsgfromDB;
    private Button sendbtn;
    private int messageCount;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Messages> messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel__one);

        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesList=new ArrayList<>();

        sendText=findViewById(R.id.input_text_msg);
        sendbtn=findViewById(R.id.msg_send_btn);
        adapter = new MyAdapter(messagesList);

        //testing...
        adapter = new MyAdapter(messagesList);
        recyclerView.setAdapter(adapter);



        //chatnode
        chatnode=FirebaseDatabase.getInstance().getReference().child("messages");
        chatnode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages=dataSnapshot.getValue(Messages.class);
                String msg=messages.getTextMessage();
                String nameofUser=messages.getUsername();
                adapter.notifyDataSetChanged();
                Messages m=dataSnapshot.getValue(Messages.class);
                messagesList.add(m);
                final Handler handler = new Handler();
                //100ms wait to scroll to item after applying changes
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                    }}, 20);
                //displayText.setText("\n"+nameofUser+": "+msg);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
                //arrayAdapter.notifyDataSetChanged();
                //listView.setSelection(listView.getAdapter().getCount()-1);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
                //arrayAdapter.notifyDataSetChanged();
                //listView.setSelection(listView.getAdapter().getCount()-1);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //adapter.notifyDataSetChanged();
                //arrayAdapter.notifyDataSetChanged();
                //listView.setSelection(listView.getAdapter().getCount()-1);
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
                if(sendstring.isEmpty())
                {
                    sendText.requestFocus();
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("messages").push().setValue(new Messages(username,sendstring));
                    sendText.setText("");
                }
            }
        });
    }

}


