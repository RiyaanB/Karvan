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

public class MeetingPreferenceSelectionActivity extends AppCompatActivity {


    public CheckBox cbInPerson;
    public CheckBox cbTheyTravel;
    public CheckBox cbOnline;

    public EditText etZip;
    public EditText etDistance;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_preference_selection);

        cbInPerson = findViewById(R.id.cb_inperson);
        cbTheyTravel = findViewById(R.id.cb_theytravel);
        cbOnline = findViewById(R.id.cb_online);

        etZip = findViewById(R.id.et_zip);
        etDistance = findViewById(R.id.et_distance);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users").child(currentUser.getUid());
    }

    public void clickMeetingPreferenceDone(View view) {

        boolean inperson = cbInPerson.isChecked();
        boolean theytravel = cbTheyTravel.isChecked();
        boolean online = cbOnline.isChecked();

        String zip = etZip.getText().toString();
        String distance = etDistance.getText().toString();

        // Validation

        reference.child("Meeting Preferences").child("InPerson").setValue(inperson);
        reference.child("Meeting Preferences").child("TheyTravel").setValue(theytravel);
        reference.child("Meeting Preferences").child("Online").setValue(online);

        reference.child("Meeting Preferences").child("ZIP").setValue(zip);
        reference.child("Meeting Preferences").child("Distance").setValue(distance);


        Intent experienceActivity = new Intent(this, ExperienceSelectActivity.class);
        startActivity(experienceActivity);
        finish();
    }
}
