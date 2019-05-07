package com.example.karvan;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ((SeekBar)findViewById(R.id.sb_searchradius)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView)findViewById(R.id.tv_searchradius)).setText(progress + " miles");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void clickSearch(View view) {
        Intent searchResultsActivityIntent = new Intent(this, SearchResultsActivity.class);
        searchResultsActivityIntent.putExtra("Service", ((TextView)findViewById(R.id.tv_selectedsrvice)).getText());
        startActivity(searchResultsActivityIntent);
    }

    public void clickSelectService(View view) {
        startActivityForResult(new Intent(this, SelectOneServiceActivity.class), 12345);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 12345)
            ((TextView)findViewById(R.id.tv_selectedsrvice)).setText(data.getStringExtra("Selected Service"));
    }
}