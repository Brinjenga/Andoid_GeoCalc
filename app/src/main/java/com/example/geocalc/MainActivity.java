package com.example.geocalc;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // instantiating input fields
    EditText p1Latitude,p1Longitude,p2Latitude,p2Longitude;

     // instantiating buttons
     Button btnCalculate,btnClear;

    // instantiating labels
    TextView distanceResult,bearingResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //connecting the view input fields to the logical input fields
        p1Latitude = findViewById(R.id.p1LatInput);
        p1Longitude = findViewById(R.id.p1LongInput);
        p2Latitude = findViewById(R.id.p2LatInput);
        p2Longitude = findViewById(R.id.p1LongInput);

        //connecting the buttons to the view buttons
        btnCalculate = findViewById(R.id.btnCalc);

        //connecting the outputs to the view labels
        distanceResult = findViewById(R.id.lblDistance);
        bearingResult = findViewById(R.id.lblBearing);

          //onClick listener that calculates distance
           btnCalculate.setOnClickListener(v -> {

            //check that input fields are not empty
            if(p1Latitude.length()==0 & p1Longitude.length()==0
                    & p2Latitude.length()==0 & p2Longitude.length()==0){
                p1Latitude.setText("Cannot be blank");
                p1Longitude.setText("Cannot be blank");
                p2Latitude.setText("Cannot be blank");
                p2Longitude.setText("Cannot be blank");
            }else{

                //get longitude and latitude inputs
                Double p1Lat = Double.valueOf(p1Latitude.getText().toString());
                Double p1Long = Double.valueOf(p1Longitude.getText().toString());
                Double p2lat = Double.valueOf(p2Latitude.getText().toString());
                Double p2long = Double.valueOf(p2Longitude.getText().toString());

                //calculate distance
               double Distance = DistanceCalculator.distance(p1Lat,p1Long,p2lat,p2long,"");

               //display Distance on Label
               distanceResult.setText(Double.toString(Distance));

            }




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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
