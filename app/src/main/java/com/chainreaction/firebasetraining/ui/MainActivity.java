package com.chainreaction.firebasetraining.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.chainreaction.firebasetraining.R;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private EditText nameText;
    private EditText lastnameText;
    private EditText ageText;
    private EditText dateText;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
    }

    private void findViews() {
        nameText = findViewById(R.id.clientName_editText);
        lastnameText = findViewById(R.id.clientLastName_editText);
        ageText = findViewById(R.id.clientAge_editText);
        dateText = findViewById(R.id.clientDate_editText);
        signOutButton = findViewById(R.id.signOut_button);
    }
}
