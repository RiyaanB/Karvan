package com.example.karvan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExperienceSelectActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference reference;

    public CheckBox cbNonNative;
    public CheckBox cbRefugees;

    public EditText etQualifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_select);

        cbNonNative = findViewById(R.id.cb_nonnative);
        cbRefugees = findViewById(R.id.cb_refugees);

        etQualifications = findViewById(R.id.et_qualifications);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users").child(currentUser.getUid());
    }

    public void clickExperienceDone(View view) {

        boolean nonnative = cbNonNative.isChecked();
        boolean refugees = cbRefugees.isChecked();

        String qualifications = etQualifications.getText().toString();

        //Validation

        reference.child("Experience").child("Non Native").setValue(nonnative);
        reference.child("Experience").child("Refugees").setValue(refugees);

        reference.child("Experience").child("Qualifications").setValue(qualifications);

        Intent writeupActivity = new Intent(this, PersonalWriteupActivity.class);
        startActivity(writeupActivity);
        finish();
    }
}
