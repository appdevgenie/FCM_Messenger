package com.appdevgenie.fcmmessenger.Services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.appdevgenie.fcmmessenger.Utils.Constants.DB_REF_TOKEN;
import static com.appdevgenie.fcmmessenger.Utils.Constants.DB_REF_USERS;

public class MyMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.d(TAG, "New message token: " + token);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child(DB_REF_USERS)
                    .child(firebaseUser.getUid())
                    .child(DB_REF_TOKEN)
                    .setValue(token);
        }
    }
}
