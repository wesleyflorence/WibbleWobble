package com.wesflorence.wibblewobble;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chat activity class for one channel in the Wibble Wobble Application.
 */
public class ChatActivity extends AppCompatActivity {
    private RecyclerView messageListView;
    private ChatMessageAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton sendButton;
    private String username;
    private String channelShow;
    private DatabaseReference chatroomDatabase;
    private EditText messageInput;
    private List<ChatMessage> messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#FF0048")));
        initialize();
//        messageInput.getBackground().setColorFilter(getResources().getColor(R.color.common_google_signin_btn_text_dark_default), PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Initializes the attributes in the chat activity class.
     */
    private void initialize() {
        Intent usernameIntent  = this.getIntent();
        this.username = usernameIntent.getStringExtra("Username");
        this.channelShow = usernameIntent.getStringExtra("Channel/Show");
        setTitle(this.channelShow);

        messagesList =  new ArrayList<>();
        messageListView = (RecyclerView) findViewById(R.id.message_list);

        // Send Message if Enter is pressed
        messageInput = (EditText) findViewById(R.id.message_input);

        // Send Message if Button is pressed
        sendButton = (FloatingActionButton) findViewById(R.id.send_mess_button);
        setOnClickListeners();
        chatroomDatabase = FirebaseDatabase.getInstance().getReference("chat");
        layoutManager = new LinearLayoutManager(ChatActivity.this);
        //layoutManager.setReverseLayout(true);
    }

    /**
     * Sets the on click listeners for the message input and the send button.
     */
    private void setOnClickListeners() {
        messageInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage();

                }
                return false;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        chatroomDatabase.child(channelShow).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!messagesList.isEmpty()){
                    messagesList.clear();
                }
                for(DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    ChatMessage message = chatSnapshot.getValue(ChatMessage.class);
                    messagesList.add(message);
                }
                chatAdapter = new ChatMessageAdapter(ChatActivity.this,
                        messagesList, username);
                messageListView.setAdapter(chatAdapter);
                messageListView.setLayoutManager(layoutManager);
                messageListView.scrollToPosition(messagesList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Sends the message to the firebase database as a chat message instance.
     */
    private void sendMessage() {
        chatroomDatabase.child(channelShow.toString())
                .push()
                .setValue(new ChatMessage(username, messageInput.getText().toString()));
        messageInput.getText().clear();
    }
}
