package com.example.karvan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class ViewingOtherProfileActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference currentUserReference;

    String name;
    String phone;
    String city;
    String intro;
    String writeup;
    String qualifications;

    ArrayList<String> servicesList;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        database = FirebaseDatabase.getInstance();

        String uid = (String) getIntent().getExtras().get("UID");
        currentUserReference = database.getReference().child("Users").child(uid);

        servicesList = new ArrayList<>();
        downloadDetails();
    }

    public void downloadDetails(){
        currentUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = (String) dataSnapshot.child("Name").getValue();
                phone = (String) dataSnapshot.child("Phone").getValue();
                city = (String) dataSnapshot.child("City").getValue();
                intro = (String) dataSnapshot.child("Introduction").getValue();
                writeup = (String) dataSnapshot.child("Experience Write Up").getValue();
                qualifications = (String) dataSnapshot.child("Qualifications").getValue();

                downloadServices();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void downloadServices(){
        currentUserReference.child("OfferingServices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> services = dataSnapshot.getChildren().iterator();

                while (services.hasNext())
                    servicesList.add(services.next().getKey());

                doneDownloading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void doneDownloading() {
        setContentView(R.layout.activity_viewing_other_profile);

        ((TextView)findViewById(R.id.tv_name)).setText(name);
        ((TextView)findViewById(R.id.tv_city)).setText(city);
        ((TextView)findViewById(R.id.tv_phone)).setText(phone);
        ((TextView)findViewById(R.id.tv_intro)).setText(intro);
        ((TextView)findViewById(R.id.tv_qualifications)).setText(qualifications);
        ((TextView)findViewById(R.id.tv_writeup)).setText(writeup);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, servicesList);
        ((ListView)findViewById(R.id.lv_services)).setAdapter(adapter);
    }
}
