package com.example.karvan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.karvan.AccountDetailsActivity;
import com.example.karvan.R;
import com.example.karvan.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(final FirebaseUser currentUser) {
        Intent userIntent;
        if(currentUser == null) {
            userIntent = new Intent(this, SignInActivity.class);
            startActivity(userIntent);
            finish();
        }
        else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(currentUser.getUid())){
                        startActivity(new Intent(StartUpActivity.this, HomeActivity.class));
//                        Intent intent = new Intent(StartUpActivity.this, ViewingOtherProfileActivity.class);
//                        intent.putExtra("UID", "eoKeghv6mkOtfV7U8g9rNTIx6y92");
//                        startActivity(intent);
//
//                        Toast.makeText(StartUpActivity.this, "Opening his profile", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(StartUpActivity.this, AccountDetailsActivity.class));
                    }
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onBackPressed() {
    }
}
