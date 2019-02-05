package com.appdevgenie.fcmmessenger.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appdevgenie.fcmmessenger.Adapters.MainListAdapter;
import com.appdevgenie.fcmmessenger.Models.User;
import com.appdevgenie.fcmmessenger.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.appdevgenie.fcmmessenger.Utils.Constants.DB_REF_USERS;
import static com.appdevgenie.fcmmessenger.Utils.Constants.PARSE_UID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main_activity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private MainListAdapter mainListAdapter;
    private ArrayList<User> users;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private String currentUser;
    //private FirebaseRecyclerAdapter<User, FirebaseUsersViewHolder> firebaseUsersAdapter;
    //private FirebaseRecyclerOptions<User> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupListener();
        setupRecyclerView();
    }

    private void setupRecyclerView() {

        users = new ArrayList<>();

        progressBar = findViewById(R.id.pbMainList);

        recyclerView = findViewById(R.id.rvUsersList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mainListAdapter = new MainListAdapter(this, users);
        recyclerView.setAdapter(mainListAdapter);

        currentUser = firebaseAuth.getCurrentUser().getUid();

        getUserListInfo();
    }

    private void getUserListInfo() {

        progressBar.setVisibility(View.VISIBLE);

        users.clear();

        Query query = databaseReference.child(DB_REF_USERS);

        /*options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        firebaseUsersAdapter = new FirebaseRecyclerAdapter<User, FirebaseUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseUsersViewHolder holder, int position, @NonNull User model) {

                holder.tvUserName.setText(model.getDisplayName());
                holder.tvUserEmail.setText(model.getEmail());

                Picasso.with(MainActivity.this)
                        .load(model.getPhotoUrl())
                        .placeholder(R.drawable.user_placeholder)
                        .into(holder.imageView);
            }

            @NonNull
            @Override
            public FirebaseUsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_main_list, viewGroup, false);
                return new FirebaseUsersViewHolder(view);
            }
        };

        recyclerView.setAdapter(firebaseUsersAdapter);*/

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);

                    if (user != null && user.getDisplayName() != null) {
                        Log.d(TAG, "onDataChange: found a user: " + user.getDisplayName());
                        if(!TextUtils.equals(currentUser, user.getUid())) {
                            users.add(user);
                        }
                    }
                }
                mainListAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void setupListener() {

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser != null){
                    //getUserListInfo();
                    //addUserToDB(firebaseUser);
                }
            }
        };
    }

//    private void addUserToDB(final FirebaseUser firebaseUser) {
//        Log.d(TAG, "addUserToDB: ");
//
//        User user = new User(
//                firebaseUser.getDisplayName(),
//                firebaseUser.getEmail(),
//                firebaseUser.getUid(),
//                firebaseUser.getPhotoUrl() == null ? "" : firebaseUser.getPhotoUrl().toString()
//        );
//
//        databaseReference.child("users")
//                .child(user.getUid()).setValue(user);
//
//        /*String token = FirebaseInstanceId.getInstance().getToken();
//        if(token != null){
//            databaseReference.child("users")
//                    .child(firebaseUser.getUid())
//                    .child("token")
//                    .setValue(token);
//        }*/
//
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String token = instanceIdResult.getToken();
//                databaseReference.child("users")
//                        .child(firebaseUser.getUid())
//                        .child("token")
//                        .setValue(token);
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_sign_out:
                firebaseAuth.signOut();
                finish();
                return true;

            case R.id.menu_refresh:
                users.clear();
                getUserListInfo();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
        //firebaseUsersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        //firebaseUsersAdapter.stopListening();
    }

    /*class FirebaseUsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView tvUserName;
        private TextView tvUserEmail;

        FirebaseUsersViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivUserPhoto);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           // String uid = users.get(getAdapterPosition()).getUid();

            Intent intent = new Intent(MainActivity.this, MessengerActivity.class);
            //intent.putExtra(PARSE_UID, uid);
            startActivity(intent);
        }
    }*/
}
