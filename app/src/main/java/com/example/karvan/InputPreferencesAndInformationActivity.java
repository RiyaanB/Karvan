package com.example.karvan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InputPreferencesAndInformationActivity extends AppCompatActivity {

    EditText introduction;

    CheckBox inperson;
    CheckBox theytraveltome;
    CheckBox online;

    EditText zipcode;
    EditText distance;

    CheckBox nonnativespeakers;
    CheckBox refugees;

    EditText qualifications;
    EditText experiencewriteup;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference currentUserDatabase;

    Bundle myBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        myBundle = getIntent().getExtras();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        currentUserDatabase = database.getReference().child("Users").child(currentUser.getUid());

        currentUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Introduction").getValue() != null) {
                    String oldIntro = (String) dataSnapshot.child("Introduction").getValue();
                    boolean oldInperson = (boolean) dataSnapshot.child("In Person").getValue();
                    boolean oldTheytravel = (boolean) dataSnapshot.child("They travel to me").getValue();
                    boolean oldOnline = (boolean) dataSnapshot.child("Online").getValue();
                    String oldZip = (String) dataSnapshot.child("Zipcode").getValue();
                    String oldDistance = (String) dataSnapshot.child("Distance").getValue();
                    boolean oldNonnativespeakers = (boolean) dataSnapshot.child("Non-native Speakers").getValue();
                    boolean oldRefugees = (boolean) dataSnapshot.child("Refugees").getValue();
                    String oldQualifications = (String) dataSnapshot.child("Qualifications").getValue();
                    String oldExperienceWriteUp = (String) dataSnapshot.child("Experience Write Up").getValue();

                    initializeInputs(oldIntro, oldInperson, oldTheytravel, oldOnline, oldZip, oldDistance, oldNonnativespeakers, oldRefugees, oldQualifications, oldExperienceWriteUp);
                } else {
                    initializeInputs();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void initializeInputs(String oldIntro, boolean oldInperson, boolean oldTheytravel, boolean oldOnline, String oldZip, String oldDistance, boolean oldNonnativespeakers, boolean oldRefugees, String oldQualifications, String oldExperienceWriteUp){

        initializeInputs();

        introduction.setText(oldIntro);

        inperson.setChecked(oldInperson);
        theytraveltome.setChecked(oldTheytravel);
        online.setChecked(oldOnline);

        zipcode.setText(oldZip);
        distance.setText(oldDistance);

        nonnativespeakers.setChecked(oldNonnativespeakers);
        refugees.setChecked(oldRefugees);

        qualifications.setText(oldQualifications);
        experiencewriteup.setText(oldExperienceWriteUp);
    }

    public void initializeInputs(){
        setContentView(R.layout.activity_input_preferences_and_information);

        introduction = (EditText) findViewById(R.id.et_intro);

        inperson = (CheckBox) findViewById(R.id.cb_inperson);
        theytraveltome = (CheckBox) findViewById(R.id.cb_theytravel);
        online = (CheckBox) findViewById(R.id.cb_online);

        zipcode = (EditText)findViewById(R.id.et_zip);
        distance = (EditText)findViewById(R.id.et_distance);

        nonnativespeakers = (CheckBox) findViewById(R.id.cb_nonnative);
        refugees = (CheckBox) findViewById(R.id.cb_refugees);

        qualifications = (EditText)findViewById(R.id.et_qualifications);
        experiencewriteup = (EditText)findViewById(R.id.et_writeup);
    }

    public void clickInputDone(View view) {

        currentUserDatabase.child("Introduction").setValue(introduction.getText().toString());

        currentUserDatabase.child("In Person").setValue(inperson.isChecked());
        currentUserDatabase.child("They travel to me").setValue(theytraveltome.isChecked());
        currentUserDatabase.child("Online").setValue(online.isChecked());

        currentUserDatabase.child("Zipcode").setValue(zipcode.getText().toString());
        currentUserDatabase.child("Distance").setValue(distance.getText().toString());

        currentUserDatabase.child("Non-native Speakers").setValue(nonnativespeakers.isChecked());
        currentUserDatabase.child("Refugees").setValue(refugees.isChecked());

        currentUserDatabase.child("Qualifications").setValue(qualifications.getText().toString());
        currentUserDatabase.child("Experience Write Up").setValue(experiencewriteup.getText().toString());

        Toast.makeText(this, "Thanks for taking the time to sign up!", Toast.LENGTH_SHORT).show();

        if(myBundle == null){
            currentUserDatabase.child("Coins").setValue(10);
        }

        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}
