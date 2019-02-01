package com.appdevgenie.fcmmessenger.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appdevgenie.fcmmessenger.Adapters.MessageListAdapter;
import com.appdevgenie.fcmmessenger.Models.Message;
import com.appdevgenie.fcmmessenger.Models.User;
import com.appdevgenie.fcmmessenger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.appdevgenie.fcmmessenger.Utils.Constants.DB_REF_MESSAGES;
import static com.appdevgenie.fcmmessenger.Utils.Constants.DB_REF_NEGATED_TIMESTAMP;
import static com.appdevgenie.fcmmessenger.Utils.Constants.DB_REF_TIMESTAMP;
import static com.appdevgenie.fcmmessenger.Utils.Constants.DB_REF_USERS;
import static com.appdevgenie.fcmmessenger.Utils.Constants.PARSE_UID;

public class MessengerActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = "messenger_activity";

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MessageListAdapter messageListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FloatingActionButton fabSend;
    private ProgressBar progressBar;
    private TextInputEditText textInputEditText;
    private DatabaseReference databaseReference;
    private String receiverUserId;
    private String senderUserId;
    private User user;
    private ArrayList<Message> messageArrayList;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messenger);

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            if(intent != null){
                receiverUserId = intent.getStringExtra(PARSE_UID);
            }
        }

        setupVariables();
        setupListener();

    }

    private void setupVariables() {

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        senderUserId = firebaseAuth.getCurrentUser().getUid();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updateToolbarTitle();

        messageArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.rvMessages);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageListAdapter = new MessageListAdapter(this, messageArrayList, senderUserId);
        recyclerView.setAdapter(messageListAdapter);

        textInputEditText = findViewById(R.id.etNewMessage);
        progressBar = findViewById(R.id.pbSendMessage);

        fabSend = findViewById(R.id.fabSendMessage);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(textInputEditText.getText()))
                    return;
                pushMessageInfo();
                textInputEditText.setText("");
            }
        });
    }

    private void pushMessageInfo() {

        long timestamp = new Date().getTime();
        long dayTimestamp = System.currentTimeMillis() + 1000;
        String messageBody = textInputEditText.getText().toString();

        Message message = new Message(messageBody, senderUserId, receiverUserId, timestamp, -timestamp, dayTimestamp);

        databaseReference
                .child(DB_REF_MESSAGES)
                //.child(receiverUserId)
                //.child(senderUserId)
                .push()
                .setValue(message);
    }

    private void setupListener() {

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(senderUserId != null){
                    populateRecyclerView();
                }
            }
        };
    }

    private void populateRecyclerView() {

        Log.d(TAG, "onDataChange: populating recycler..." );

        progressBar.setVisibility(View.VISIBLE);

        Query query = databaseReference
                .child(DB_REF_MESSAGES)
                //.child(receiverUserId)
                //.child(senderUserId)
                .orderByChild(DB_REF_TIMESTAMP);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageArrayList.clear();

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Message message = data.getValue(Message.class);
                    if(senderUserId != null){
                        Log.d(TAG, "onDataChange: found a message: " + message.getBody());
                        messageArrayList.add(message);
                    }
                }
                messageListAdapter.notifyDataSetChanged();
                linearLayoutManager.scrollToPosition(messageArrayList.size() -1);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                for(DataSnapshot data : dataSnapshot.getChildren()){
//                    Message message = data.getValue(Message.class);
//                    if(senderUserId != null){
//                        Log.d(TAG, "onDataChange: found a message: " + message.getBody());
//                        messageArrayList.add(message);
//                    }
//                }
//                messageListAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot data : dataSnapshot.getChildren()){
//                    Message message = data.getValue(Message.class);
//                    if(senderUserId != null){
//                        Log.d(TAG, "onDataChange: found a message: " + message.getBody());
//                        messageArrayList.add(message);
//                    }
//                }
//                messageListAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        recyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                recyclerView.smoothScrollToPosition(messageArrayList.size());
//            }
//        });
    }

    private void updateToolbarTitle() {

        Query query = databaseReference.child(DB_REF_USERS).child(receiverUserId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    toolbar.setTitle(user.getDisplayName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
