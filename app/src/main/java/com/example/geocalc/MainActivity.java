package com.example.geocalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText p1Latitude, p1Longitude, p2Latitude, p2Longitude;   // instantiating input fields
    Button btnCalculate, btnClear;    // instantiating buttons
    TextView distanceResult, bearingResult;   // creating label variables
    public static final int code = 1; //value to return after distance units selection
    Double Distance;
    Double Bearing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        p1Latitude = findViewById(R.id.p1LatInput);
        p1Longitude = findViewById(R.id.p1LongInput);   //connecting the view input fields to the variable fields
        p2Latitude = findViewById(R.id.p2LatInput);
        p2Longitude = findViewById(R.id.p2LongInput);
        btnCalculate = findViewById(R.id.btnCalc);       //connecting the buttons to the view buttons
        btnClear = findViewById(R.id.btnClear);
        distanceResult = findViewById(R.id.lblDistance);
        bearingResult = findViewById(R.id.lblBearing);   //connecting the outputs to the view labels


        //onClick listener that calculates distance
        btnCalculate.setOnClickListener(v -> {
            if (p1Latitude.length() == 0 & p1Longitude.length() == 0
                    & p2Latitude.length() == 0 & p2Longitude.length() == 0) {     //check that input fields are not empty
                p1Latitude.setText("Cannot be blank");
                p1Longitude.setText("Cannot be blank");
                p2Latitude.setText("Cannot be blank");
                p2Longitude.setText("Cannot be blank");
            } else {
                double p1Lat = Double.valueOf(p1Latitude.getText().toString());   //get longitude and latitude inputs
                double p1Long = Double.valueOf(p1Longitude.getText().toString());
                double p2lat = Double.valueOf(p2Latitude.getText().toString());
                double p2long = Double.valueOf(p2Longitude.getText().toString());
                DecimalFormat f = new DecimalFormat("#.##");              //trim doubles to 2 decimal places


                Distance = DistanceCalculator.distance(p1Lat, p1Long, p2lat, p2long);   //calculate distance in km
                distanceResult.setText("Distance: " + f.format(Distance) + " Kilometers");        //display Distance on Label

                Bearing = BearingCalculator.bearing(p1Lat, p1Long, p2lat, p2long);    //calculate bearing
                bearingResult.setText("Bearing: " + f.format(Bearing) + " Degrees");               //display Bearing on Label
            }
        });

        //onClick listener that clears  values
        btnClear.setOnClickListener(v -> {
            p1Latitude.setText("");
            p1Longitude.setText("");          //set input fields to blank
            p2Latitude.setText("");
            p2Longitude.setText("");
            distanceResult.setText("Distance: ");   //set labels back to original text
            bearingResult.setText("Bearing: ");
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, MySettings.class);
            startActivityForResult(intent, code);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DecimalFormat f = new DecimalFormat("#.##");
        String dunits="";
        String bunits="";
        if (resultCode == code) {
            dunits = data.getStringExtra("dOptions");
            bunits = data.getStringExtra("bOptions");
            if(dunits.compareTo("Miles") == 0 ){
                Double distance= Distance * 0.621371;
                distanceResult.setText("Distance: " + f.format(distance) + " " + dunits);
            }else{
                distanceResult.setText("Distance: " + f.format(Distance) + " " + dunits);
            }
            if(bunits.compareTo("Mils") == 0 ){
                Double bearing= Bearing * 17.777777777778;
                bearingResult.setText(f.format(bearing) + " " + bunits);
            }else{
                bearingResult.setText(f.format(Bearing) + " " + bunits);
            }

        }
        else {
                distanceResult.setText("Distance: " + f.format(Distance) +" " +  dunits);
                bearingResult.setText(f.format(Distance) + " " + bunits);
            }
        }
    }


