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
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;

import com.appdevgenie.fcmmessenger.Models.User;
import com.appdevgenie.fcmmessenger.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.appdevgenie.fcmmessenger.Utils.Constants.DB_REF_USERS;
import static com.appdevgenie.fcmmessenger.Utils.Constants.PARSE_UID;

public class MessengerActivity extends AppCompatActivity implements TextWatcher {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fabSend;
    private ProgressBar progressBar;
    private TextInputEditText textInputEditText;
    private DatabaseReference databaseReference;
    private String uid;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messenger);

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            if(intent != null){
                uid = intent.getStringExtra(PARSE_UID);
            }
        }

        setupVariables();

    }

    private void setupVariables() {

        databaseReference = FirebaseDatabase.getInstance().getReference();

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

        recyclerView = findViewById(R.id.rvMessages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressBar = findViewById(R.id.pbSendMessage);
        fabSend = findViewById(R.id.fabSendMessage);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void updateToolbarTitle() {

        Query query = databaseReference.child(DB_REF_USERS).child(uid);

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
}
