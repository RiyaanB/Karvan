package com.example.karvan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectOneServiceActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    ArrayAdapter<Category> adapter;
    ArrayList<Category> categories;

    ArrayList<String> selectedServices;
    ArrayList<String> initialSelected;

    Bundle myBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        myBundle = getIntent().getExtras();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        selectedServices = new ArrayList<>();
        initialSelected = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").child(currentUser.getUid()).child("OfferingServices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    String service = iterator.next().getKey();
                    selectedServices.add(service);
                    initialSelected.add(service);
                }

                downloadFile();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void downloadFile() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("Services.txt");

        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String text = new String(bytes);
                makeTree(text);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SelectOneServiceActivity.this, "I'm Toast", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void makeTree(String text) {
        String[] cats = text.split("\n");
        categories = new ArrayList<>();
        selectedServices = new ArrayList<>();

        for (int i = 0; i < cats.length; i++) {
            if (cats[i].charAt(0) == '\t') {
                String service = cats[i].substring(1);
                categories.get(categories.size() - 1).services.add(service);
                if (initialSelected.contains(service)) {
                    categories.get(categories.size() - 1).selected.add(service);
                    categories.get(categories.size() - 1).idk[categories.get(categories.size() - 1).num] = true;
                    categories.get(categories.size() - 1).num += 1;
                    selectedServices.add(service);
                }
            } else {
                categories.add(new Category(cats[i]));
            }
        }
        Toast.makeText(this, "Len: " + initialSelected.size(), Toast.LENGTH_SHORT).show();

        doneDownloading();
    }

    private void doneDownloading() {
        setContentView(R.layout.activity_offering_skills_selection);

        final ListView categoryListView = findViewById(R.id.lv_categories);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        categoryListView.setAdapter(adapter);


        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectOneServiceActivity.this, R.style.MyDialogTheme);

                builder.setTitle("Select the services you're offering");

                builder.setItems(categories.get(position).services.toArray(new String[categories.get(position).services.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("Selected Service", categories.get(position).services.get(which));
                        setResult(12345, intent);
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.show();
            }
        });


        EditText searchbarEditText = findViewById(R.id.et_searchbar);
        searchbarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void clickCategoryDone(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userReference = database.getReference("Users").child(currentUser.getUid()).child("OfferingServices");
        DatabaseReference serviceReference = database.getReference("Services");


        for (String service : initialSelected) {
            if (!selectedServices.contains(service)) {
                userReference.child(service).removeValue();
                serviceReference.child(service).removeValue();
            }
        }

        for (String service : selectedServices) {
            userReference.child(service).setValue("");
            serviceReference.child(service).child(currentUser.getUid()).setValue("");
        }


        if (myBundle != null) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, InputPreferencesAndInformationActivity.class));
        }
        finish();
    }


    @Override
    public void onBackPressed() {
    }
}