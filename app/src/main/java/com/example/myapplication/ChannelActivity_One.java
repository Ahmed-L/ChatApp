package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChannelActivity_One extends AppCompatActivity {

    FirebaseAuth Fdb;
    DatabaseReference FdbRef;
    DatabaseReference chatnode,childCounterRef;
    private EditText sendText;
    private String sendstring,username;
    private Button sendbtn;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Messages> messagesList;
    long totalChild; //This is used to store last message key from the database;
    String lastKey="",messageKey="";
    boolean lastKeySet =true;
    int y=10,counter=0,counter2=0;

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
        recyclerView.setAdapter(adapter);

        childCounterRef=FirebaseDatabase.getInstance().getReference();
        childCounterRef.child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalChild = dataSnapshot.getChildrenCount();
                //System.out.println(totalChild);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //chatnode is used for retrieving the messages and displaying them
        chatnode=FirebaseDatabase.getInstance().getReference().child("messages");
        Query query = chatnode.orderByChild("date").limitToLast(10);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages m=dataSnapshot.getValue(Messages.class);

                if(!messagesList.isEmpty() && !messagesList.contains(m)) {
                    messagesList.add(m);
                    adapter.notifyDataSetChanged();
                    System.out.println(m.getTextMessage().toString() + "ADDED BY INITIAL LOADER");
                }
                else if(messagesList.isEmpty())
                {
                    messagesList.add(m);
                    adapter.notifyDataSetChanged();
                    System.out.println(m.getTextMessage().toString() + "ADDED BY INITIAL LOADER WHEN MT");
                }

                counter++;
                if(counter==1)
                {
                    messageKey=m.getDate();
                    System.out.println(messageKey+" << Date taken from initial load");
                }

                final Handler handler = new Handler();
                //100ms wait to scroll to item after applying changes
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                    }}, 50);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
                //arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(linearLayoutManager.findFirstVisibleItemPosition()==0 && counter==y)
                {

                    if(counter==(y))
                    {
                        counter=0;
                        y=10;
                        lastKey=messageKey;
                    }
                    chatnode.orderByChild("date").endAt(messageKey).limitToLast(y).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            Messages m = dataSnapshot.getValue(Messages.class);
                            if(!m.getDate().equals(lastKey))
                            {
                                messagesList.add(counter++, m);
                                adapter.notifyDataSetChanged();
                                System.out.println(m.getTextMessage().toString()+" ADDED BY SCROLLER!!!");
                            }
                            else if(m.getDate().equals(lastKey))
                            {
                                lastKey=messageKey;
                                counter++;
                            }

                            //System.out.println(y+" is the value of Y ");
                            //System.out.println(counter+" is the value of count ");
                            if(counter==1)
                            {
                                messageKey=m.getDate();
                               // System.out.println(messageKey+" DATE taken from scroll");
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            System.out.println("ON CHILD CHANGED");

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });


        //Fdb
        Fdb=FirebaseAuth.getInstance();
        FirebaseUser user_now=FirebaseAuth.getInstance().getCurrentUser();
        String userDB_ID= Objects.requireNonNull(user_now).getUid();
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

    //public void loadOlder(void)
    //{

    //}

}


