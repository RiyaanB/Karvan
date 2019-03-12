package com.example.karvan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalWriteupActivity extends AppCompatActivity {


    public EditText etWriteup;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_writeup);

        etWriteup = findViewById(R.id.et_writeup);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users").child(currentUser.getUid());
    }

    public void clickWriteupDone(View view) {

        String writeup = etWriteup.getText().toString();

        // Validation

        reference.child("Write Up").setValue(writeup);

        Intent detailsActivity = new Intent(this, AccountDetailsActivity.class);
        startActivity(detailsActivity);
        finish();
    }
}
