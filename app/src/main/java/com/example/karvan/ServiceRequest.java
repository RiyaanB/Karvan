package com.example.karvan;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class ServiceRequest {

    String name;
    String uid;
    String service;

    public ServiceRequest(String key, String value) {
        uid = key;
        service = value;
    }
}
