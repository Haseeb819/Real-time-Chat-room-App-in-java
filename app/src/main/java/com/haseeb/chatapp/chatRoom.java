package com.haseeb.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.haseeb.chatapp.Models.ALLMethods;
import com.haseeb.chatapp.Models.Message;
import com.haseeb.chatapp.Models.Users;
import com.haseeb.chatapp.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class chatRoom extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    Users u;
    List<Message> messages;

    RecyclerView rvmsg;
    EditText etmsg;
    ImageButton imgbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        init();
    }
    private void init(){
        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        u=new Users();

        rvmsg=findViewById(R.id.rvmsg);
        etmsg=findViewById(R.id.etmsg);
        imgbtn=findViewById(R.id.imgbtn);
        imgbtn.setOnClickListener(this);
        messages=new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
          if(!TextUtils.isEmpty(etmsg.getText().toString()))
          {
              Message message = new Message(etmsg.getText().toString(),u.getName());
              etmsg.setText("");
              messagedb.push().setValue(message);
          }
          else
          {
              Toast.makeText(getApplicationContext(), "You can't send blank message ",
                      Toast.LENGTH_SHORT).show();
          }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout)
        {
           auth.signOut();
           finish();
           startActivity(new Intent(chatRoom.this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();

        u.setUid(currentUser.getUid());
        u.setEmail(currentUser.getEmail());

        database.getReference("Users").child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u = snapshot.getValue(Users.class);
                u.setUid(currentUser.getUid());
                ALLMethods.name = u.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagedb = database.getReference("messages");
        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                messages.add(message);
                displayMessages(messages);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());

                List<Message> newMessages = new ArrayList<Message>();

                for(Message m: messages)
                {
                    if(m.getKey().equals(message.getKey()))
                    {
                        newMessages.add(message);
                    }
                    else
                    {
                        newMessages.add(m);
                    }
                }

                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());

                List<Message> newMessages = new ArrayList<Message>();

                for(Message m: messages)
                {
                    if(!m.getKey().equals(message.getKey()))
                    {
                        newMessages.add(m);
                    }
                }

                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();

    }

    private void displayMessages(List<Message> messages)
    {
        rvmsg.setLayoutManager(new LinearLayoutManager(chatRoom.this));
        messageAdapter = new MessageAdapter(chatRoom.this,messages,messagedb);
        rvmsg.setAdapter(messageAdapter);
    }
}