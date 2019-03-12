package com.example.karvan;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class CategorySelectTwoActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    ArrayList<String> subCategories;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        subCategories = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        String thisCategory = getIntent().getExtras().getString("Category");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Categories").child(thisCategory);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> category_iterator = dataSnapshot.getChildren().iterator();

                while(category_iterator.hasNext())
                    subCategories.add(category_iterator.next().getKey());

                doneDownloading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void doneDownloading() {
        setContentView(R.layout.activity_category_select_two);

        ListView subcategoryListView = findViewById(R.id.lv_subcategories);

        //
        //
        // Replace layout with custom toggle-able layout
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subCategories);
        //
        //


        subcategoryListView.setAdapter(adapter);

        EditText subcategorySearchBar = findViewById(R.id.et_subsearchbar);
        subcategorySearchBar.addTextChangedListener(new TextWatcher() {
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

        subcategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Toggle Service
                //
                //
                //

            }
        });
    }

    public void clickDoneSub(View view) {
        finish();
    }
}
