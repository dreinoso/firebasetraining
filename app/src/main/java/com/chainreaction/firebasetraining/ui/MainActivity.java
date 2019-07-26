package com.chainreaction.firebasetraining.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chainreaction.firebasetraining.R;
import com.chainreaction.firebasetraining.ui.firebase.DemoClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.chainreaction.firebasetraining.ui.LoginActivity.GOOGLE_USER_ID_TAG;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private EditText nameText;
    private EditText lastNameText;
    private EditText ageText;
    private EditText dateText;
    private Button signOutButton;
    private Button submitButton;

    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        linkButtons();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = getIntent().getStringExtra(GOOGLE_USER_ID_TAG);
        updateClientValues();
    }

    private void updateClientValues() {
        mDatabase.child("clients").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        DemoClient demoClient = dataSnapshot.getValue(DemoClient.class);

                        if (demoClient != null) {
                            Log.d(TAG, "updateClientValues: onDataChanged views updated");
                            nameText.setText(demoClient.name);
                            lastNameText.setText(demoClient.lastName);
                            ageText.setText(demoClient.age);
                            dateText.setText(demoClient.date);
                        } else {
                            Log.d(TAG, "updateClientValues: onDataChanged no client registered yet");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void findViews() {
        nameText = findViewById(R.id.clientName_editText);
        lastNameText = findViewById(R.id.clientLastName_editText);
        ageText = findViewById(R.id.clientAge_editText);
        dateText = findViewById(R.id.clientDate_editText);
        signOutButton = findViewById(R.id.signOut_button);
        submitButton = findViewById(R.id.submit_button);
    }

    private void linkButtons() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void submitData() {
        final String name = nameText.getText().toString();
        final String lastName = lastNameText.getText().toString();
        final String age = ageText.getText().toString();
        final String date = dateText.getText().toString();

        if ( TextUtils.isEmpty(name) ||
             TextUtils.isEmpty(lastName) ||
             TextUtils.isEmpty(age) ||
             TextUtils.isEmpty(date) ) {
            Toast.makeText(this, R.string.all_field_required,Toast.LENGTH_LONG);
            return;
        }

        Toast.makeText(this, getResources().getString(R.string.posting_message), Toast.LENGTH_SHORT).show();
        insertNewClient(userId, name, lastName, age, date);
    }

    private void insertNewClient(String userId, String name, String lastName, String age, String date) {
        Log.d(TAG, "insertNewClient");
        //mDatabase.setValue(newClient);

        String key = mDatabase.child("clients").push().getKey();
        //Post post = new Post(userId, name, lastName, age, date);
        DemoClient newClient = new DemoClient();
        newClient.setValue(userId, name, lastName, age, date);
        Map<String, Object> clientValues = newClient.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/clients/" + userId , clientValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, clientValues);
        mDatabase.updateChildren(childUpdates);
    }

    private void updateClient(DemoClient demoClient, String name, String lastName, String age, String date) {
        Log.d(TAG, "updateClient");
        //mDatabase.setValue(newClient);

        demoClient.name = name;
        demoClient.lastName = lastName;
        demoClient.age = age;
        demoClient.date = date;

        Map<String, Object> clientValues = demoClient.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/clients/" + userId , clientValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, clientValues);
        mDatabase.updateChildren(childUpdates);
    }

    public String getUid() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "getUid " + userId);
        return userId;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        String clientId = getResources().getString(R.string.default_web_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build();
        GoogleSignIn.getClient(this, gso).signOut();
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
    }
}
