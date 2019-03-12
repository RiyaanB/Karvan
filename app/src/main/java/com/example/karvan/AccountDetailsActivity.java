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

public class AccountDetailsActivity extends AppCompatActivity {


    public EditText etFirstName;
    public EditText etLastName;
    public EditText etPhone;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        etFirstName = findViewById(R.id.input_fn);
        etLastName = findViewById(R.id.input_ln);
        etPhone = findViewById(R.id.input_phone);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users").child(currentUser.getUid());

        etFirstName.setText(currentUser.getDisplayName() == null ? "" : currentUser.getDisplayName().split(" ")[0]);
        etLastName.setText(currentUser.getDisplayName() == null ? "" : currentUser.getDisplayName().split(" ")[1]);
        etPhone.setText(currentUser.getPhoneNumber() == null ? "" : currentUser.getPhoneNumber());
    }

    public void clickDetailsDone(View view) {

        String firstname = etFirstName.getText().toString();
        String lastname = etLastName.getText().toString();
        String phone = etPhone.getText().toString();

        reference.child("First Name").setValue(firstname);
        reference.child("Last Name").setValue(lastname);
        reference.child("Phone").setValue(phone);

        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}
