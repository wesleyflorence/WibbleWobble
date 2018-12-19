package com.wesflorence.wibblewobble;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.wesflorence.wibblewobble.R;

/**
 * Represents a room list activity that shows the chat rooms for each tv show.
 */
public class RoomListActivity extends AppCompatActivity {
    DatabaseReference showsDatabase;
    ListView showListView;
    List<TvShow> tvShowList;
    ArrayAdapter<TvShow> adapter;
    ImageView loadingIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        loadingIcon = (ImageView) findViewById(R.id.wibbleart);
        loadingIcon.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(R.string.whatsOnNow);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#FF0048")));
        tvShowList = new ArrayList<>();
        showsDatabase = FirebaseDatabase.getInstance().getReference("shows");
        showListView = findViewById(R.id.showsListView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        showsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setListAdapter(dataSnapshot);

                showListView.setAdapter(adapter);
                showListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        createDialogue(position);
                    }
                });
                loadingIcon.setVisibility(View.INVISIBLE);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Initializes the list view adapter.
     * @param dataSnapshot the data snapshot
     */
    private void setListAdapter(DataSnapshot dataSnapshot) {
        if (!tvShowList.isEmpty()){
            tvShowList.clear();
        }
        for(DataSnapshot showSnapshot : dataSnapshot.getChildren()) {
            TvShow show = showSnapshot.getValue(TvShow.class);
            tvShowList.add(show);
            System.out.println(show.getShow());
        }
        adapter = new ArrayAdapter<>(RoomListActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, tvShowList);
    }

    /**
     * Creates and shows the dialogue box for username input to be passed to the chat activity with
     * the chosen show.
     * @param position the position of the chosen show
     */
    private void createDialogue(int position) {
        AlertDialog.Builder usernameDialog = new AlertDialog.Builder(
                RoomListActivity.this);
        usernameDialog.setTitle("Username Submission");
        usernameDialog.setMessage("Enter username");

        final EditText input = new EditText(RoomListActivity.this);
        input.setMaxLines(1);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        usernameDialog.setView(input);
        final String text = showListView.getItemAtPosition(position).toString();
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    Intent intent = addToBundle(input, text);
                    startActivity(intent);
                }
                return false;
            }
        });
        usernameDialog.setPositiveButton("Submit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = addToBundle(input, text);
                        startActivity(intent);
                    }
                });
        usernameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        usernameDialog.show();
    }

    /**
     * Adds the username and show name to the next intent's bundle.
     * @param input the username's text field
     * @param text the name of the show
     * @return the intent
     */
    private Intent addToBundle(EditText input, String text) {
        String username = input.getText().toString();
        Intent intent = new Intent(
                RoomListActivity.this,
                ChatActivity.class);
        intent.putExtra("Username", username);
        intent.putExtra("Channel/Show", text);
        return intent;
    }
}
