package com.example.karvan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewRequestsActivity extends AppCompatActivity {

    ListView lv_serviceRequests;

    ArrayList<ServiceRequest> serviceRequests;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        serviceRequests = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("Requests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()){
                    DataSnapshot dataSnapshot1 = iterator.next();
                    serviceRequests.add(new ServiceRequest(dataSnapshot1.getKey(), (String) dataSnapshot1.getValue()));
                }

                onDownloadComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onDownloadComplete() {
        setContentView(R.layout.activity_view_requests);

        lv_serviceRequests = findViewById(R.id.lv_requests);

        RequestsAdapter adapter = new RequestsAdapter(this, R.layout.list_item_request, serviceRequests);
        lv_serviceRequests.setAdapter(adapter);

        lv_serviceRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewRequestsActivity.this);
                builder.setMessage("Are you sure you want to accept this request? Once accepted, the transaction will occur immediately").setPositiveButton("Accept service request", dialogClickListener).setNegativeButton("Reject service request", dialogClickListener).show();
            }
        });
    }

    class RequestsAdapter extends ArrayAdapter<ServiceRequest>{
        public RequestsAdapter(Context context, int resource, List<ServiceRequest> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null){
                LayoutInflater li = getLayoutInflater();
                convertView = li.inflate(R.layout.list_item_request, null);
                holder = new ViewHolder();
                holder.serviceName = convertView.findViewById(R.id.tv_servicename);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ServiceRequest current = serviceRequests.get(position);

            holder.serviceName.setText(current.service);
            return convertView;
        }
    }

    class ViewHolder{
        TextView serviceName;
    }
}
