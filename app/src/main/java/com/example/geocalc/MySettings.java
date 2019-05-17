package com.example.geocalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MySettings extends AppCompatActivity {

        private String optionKm = "Kilometers";    //default distance selection
        private String optionDegrees = "Degrees";      // default bearing selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("bOptions",optionKm);
            setResult(MainActivity.Dunits);
            finish();
        });

        Spinner distanceUnits,bearingUnits;
        distanceUnits = findViewById(R.id.distanceUnits);  //creating a spinner variable
        bearingUnits = findViewById(R.id.bearingUnits);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this,
             R.array.bOptions, android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceUnits.setAdapter(adapter);
        distanceUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                optionKm = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
