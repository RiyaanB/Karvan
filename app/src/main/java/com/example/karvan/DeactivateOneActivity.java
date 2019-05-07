package com.example.karvan;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class DeactivateOneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_one);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void clickDeactivateConfirm(View view) {
        FirebaseDatabase.getInstance().getReference().child("Feedback").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(((EditText)findViewById(R.id.et_feedback)).getText());
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);
        FirebaseAuth.getInstance().getCurrentUser().delete();
        finishAffinity();
        startActivity(new Intent(this, DeactivateTwoActivity.class));
    }
}
