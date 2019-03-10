package com.example.karvan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccountDetailsActivity extends AppCompatActivity {

    private EditText firstnameEditText;
    private EditText lastnameEditText;
    private EditText genderEditText;
    private EditText introductionEditText;
    private EditText qualificationsEditText;
    private EditText websiteEditText;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_details);

        firstnameEditText = findViewById(R.id.input_firstname);
        lastnameEditText = findViewById(R.id.input_lastname);
        genderEditText = findViewById(R.id.input_gender);
        introductionEditText = findViewById(R.id.input_introduction);
        qualificationsEditText = findViewById(R.id.input_qualifications);
        websiteEditText = findViewById(R.id.input_website);

        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishClick(v);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUser.getUid())){
                    dataSnapshot = dataSnapshot.child(currentUser.getUid());
                    Object firstname = dataSnapshot.child("First Name").getValue();
                    Object lastname = dataSnapshot.child("Last Name").getValue();
                    Object gender = dataSnapshot.child("Gender").getValue();
                    Object introduction = dataSnapshot.child("Introduction").getValue();
                    Object qualifications = dataSnapshot.child("Qualifications").getValue();
                    Object website = dataSnapshot.child("Website").getValue();

                    firstnameEditText.setText(firstname == null ? "" : firstname.toString());
                    lastnameEditText.setText(lastname == null ? "" : lastname.toString());
    introductionEditText.setText(introduction == null ? "" : introduction.toString());
                    qualificationsEditText.setText(qualifications == null ? "" : qualifications.toString());
                    websiteEditText.setText(website == null ? "" : website.toString());
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void finishClick(View view) {

        DatabaseReference reference1 = database.getReference("Users").child(currentUser.getUid());
        reference1.child("First Name").setValue(firstnameEditText.getText().toString());
        reference1.child("Last Name").setValue(lastnameEditText.getText().toString());
        reference1.child("Gender").setValue(genderEditText.getText().toString());
        reference1.child("Introduction").setValue(introductionEditText.getText().toString());
        reference1.child("Qualifications").setValue(qualificationsEditText.getText().toString());
        reference1.child("Website").setValue(websiteEditText.getText().toString());

        startActivity(new Intent(this, MainActivity.class));

        finish();
    }
}
