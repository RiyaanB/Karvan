package com.example.karvan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    ListView lv_searchResults;

    ArrayList<SearchResult> searchResults;

    String currentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        currentService = getIntent().getExtras().getString("Service");


        //debug
        ArrayList<SearchResult> searchResults = null;
        onDownloadComplete();
    }


    public void onDownloadComplete(){
        setContentView(R.layout.activity_search_results);

        lv_searchResults = findViewById(R.id.lv_searchresults);

        if(searchResults == null){
            searchResults = new ArrayList<>();
            SearchResult dummy1 = new SearchResult("eoKeghv6mkOtfV7U8g9rNTIx6y92", "John Numberone", "9820449079", "johndoe1@gmail.com", "I am very nice");
            SearchResult dummy2 = new SearchResult("unumunuh", "John Numbertwo", "9819450109", "johndoe2@gmail.com", "I am quite smart");
            searchResults.add(dummy1);
            searchResults.add(dummy2);
        }

        SearchResultsAdapter adapter = new SearchResultsAdapter(this, R.layout.list_item_searchresult, searchResults);
        lv_searchResults.setAdapter(adapter);

        lv_searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent viewProfileIntent = new Intent(SearchResultsActivity.this, ViewingOtherProfileActivity.class);
                                viewProfileIntent.putExtra("UID", searchResults.get(position).uid);
                                startActivity(viewProfileIntent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                sendServiceRequest(position);
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultsActivity.this);
                builder.setMessage("What do you want do?").setNegativeButton("Send service request", dialogClickListener).setPositiveButton("View Profile", dialogClickListener).show();
            }
        });
    }

    private void sendServiceRequest(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        String uid_of_recipient = searchResults.get(position).uid;
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Users").child(uid_of_recipient).child("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(currentService);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want request this service? Once accepted, the transaction will occur immediately").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    class SearchResultsAdapter extends ArrayAdapter<SearchResult>{

        public SearchResultsAdapter(Context context, int resource, List<SearchResult> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null){
                LayoutInflater li = getLayoutInflater();
                convertView = li.inflate(R.layout.list_item_searchresult, null);
                holder = new ViewHolder();
                holder.name = (TextView)convertView.findViewById(R.id.tv_name);
                holder.email = (TextView)convertView.findViewById(R.id.tv_email);
                holder.phone = (TextView)convertView.findViewById(R.id.tv_phone);
                holder.intro = (TextView)convertView.findViewById(R.id.tv_intro);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            SearchResult current = searchResults.get(position);
            holder.name.setText(current.name);
            holder.email.setText(current.email);
            holder.phone.setText(current.phone);
            holder.intro.setText(current.intro);
            return convertView;
        }
    }

    class ViewHolder{
        TextView name, phone, email, intro;
    }
}
