package com.example.geocalc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geocalc.webservice.WeatherService;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.geocalc.webservice.WeatherService.BROADCAST_WEATHER;

public class MainActivity extends AppCompatActivity {

    EditText p1Latitude, p1Longitude, p2Latitude, p2Longitude;   // instantiating input fields
    Button btnCalculate, btnClear;    // instantiating buttons
    TextView distanceResult, bearingResult;   // creating label variables
    public static final int code = 1; //value to return after distance units selection
    public static int HISTORY_RESULT = 2;
    public static int SEARCH_RESULT = 3;
    String distUnits, bearUnits;
    DatabaseReference topRef;
    public static List<LocationLookup> allHistory;

    @BindView(R.id.weatherIcon1) ImageView p1Icon;
    @BindView(R.id.weatherIcon2) ImageView p2Icon;
    @BindView(R.id.temp1) TextView p1Temp;
    @BindView(R.id.temp2) TextView p2Temp;
    @BindView(R.id.descr1) TextView p1Summary;
    @BindView(R.id.descr2) TextView p2Summary;

    @BindView(R.id.search_button) Button btnSearch;

    private BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            double temp = bundle.getDouble("TEMPERATURE");
            String summary = bundle.getString("SUMMARY");
            String icon = bundle.getString("ICON").replaceAll("-", "_");
            String key = bundle.getString("KEY");
            int resID = getResources().getIdentifier(icon , "drawable", getPackageName());
            setWeatherViews(View.VISIBLE);
            if (key.equals("p1"))  {
                p1Summary.setText(summary);
                p1Temp.setText(Double.toString(temp));
                p1Icon.setImageResource(resID);
            } else {
                p2Summary.setText(summary);
                p2Temp.setText(Double.toString(temp));
                p2Icon.setImageResource(resID);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Places.initialize(getApplicationContext(), BuildConfig.PLACES_API_KEY);
        allHistory = new ArrayList<LocationLookup>();

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

        ButterKnife.bind(this);

        distUnits = "Kilometers";
        bearUnits = "Degrees";

        //onClick listener that calculates distance
        btnCalculate.setOnClickListener(v -> {
            // remember the calculation.
            updateCalcs(true);
            closeKeyboard();
        });

        //onClick listener that clears  values
        btnClear.setOnClickListener(v -> {
            p1Latitude.setText("");
            p1Longitude.setText("");          //set input fields to blank
            p2Latitude.setText("");
            p2Longitude.setText("");
            distanceResult.setText("Distance: ");   //set labels back to original text
            bearingResult.setText("Bearing: ");
            setWeatherViews(View.INVISIBLE);
            closeKeyboard();
        });
    }


    @Override
    public void onResume(){
        super.onResume();
        allHistory.clear();
        IntentFilter weatherFilter = new IntentFilter(BROADCAST_WEATHER);
        LocalBroadcastManager.getInstance(this).registerReceiver(weatherReceiver, weatherFilter);
        topRef = FirebaseDatabase.getInstance().getReference("history");
        topRef.addChildEventListener (chEvListener);
        //topRef.addValueEventListener(valEvListener);
        setWeatherViews(View.INVISIBLE);
    }

    @Override
    public void onPause(){
        super.onPause();
        topRef.removeEventListener(chEvListener);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weatherReceiver);
    }

    private void setWeatherViews(int visible) {
        p1Icon.setVisibility(visible);
        p2Icon.setVisibility(visible);
        p1Summary.setVisibility(visible);
        p2Summary.setVisibility(visible);
        p1Temp.setVisibility(visible);
        p2Temp.setVisibility(visible);
    }

    @OnClick(R.id.search_button)
    public void searchPressed() {
        Intent intent = new Intent(MainActivity.this, LocationSearchActivity.class);
        startActivityForResult(intent, SEARCH_RESULT);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager inputM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputM.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
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
            intent.putExtra("distUnits", distUnits);
            intent.putExtra("bearUnits", bearUnits);
            startActivityForResult(intent, code);
            return true;
        } else if(item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, HISTORY_RESULT );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == code) {
            distUnits = data.getStringExtra("dOptions");
            bearUnits = data.getStringExtra("bOptions");
            updateCalcs(false);
        } else if (resultCode == HISTORY_RESULT) {
            Bundle extras = data.getExtras();
           double [] locations = data.getDoubleArrayExtra("locationItem");
            this.p1Latitude.setText(Double.valueOf(locations[0]).toString());
            this.p1Longitude.setText(Double.valueOf(locations[1]).toString());
            this.p2Latitude.setText(Double.valueOf(locations[2]).toString());
            this.p2Longitude.setText(Double.valueOf(locations[3]).toString());
            this.updateCalcs(false);
        } else if (requestCode == SEARCH_RESULT) {
            Bundle extras = data.getExtras();
            LocationLookup ll = Parcels.unwrap(extras.getParcelable("ll"));
            this.p1Latitude.setText(String.valueOf(ll.origLat));
            this.p1Longitude.setText(String.valueOf(ll.origLng));
            this.p2Latitude.setText(String.valueOf(ll.endLat));
            this.p2Longitude.setText(String.valueOf(ll.endLng));
            this.updateCalcs(true);
        }
    }

    private void updateCalcs(boolean save) {

        if (p1Latitude.length() == 0 & p1Longitude.length() == 0
                & p2Latitude.length() == 0 & p2Longitude.length() == 0) {     //check that input fields are not empty
            /* Do nothing */
        } else {
            double p1Lat = Double.valueOf(p1Latitude.getText().toString());   //get longitude and latitude inputs
            double p1Long = Double.valueOf(p1Longitude.getText().toString());
            double p2lat = Double.valueOf(p2Latitude.getText().toString());
            double p2long = Double.valueOf(p2Longitude.getText().toString());

            WeatherService.startGetWeather(this, Double.toString(p1Lat), Double.toString(p1Long), "p1");
            WeatherService.startGetWeather(this, Double.toString(p2lat), Double.toString(p2long), "p2");

            if (save) {

                LocationLookup entry = new LocationLookup();
                entry.setOrigLat(p1Lat);
                entry.setOrigLng(p1Long);
                entry.setEndLat(p2lat);
                entry.setEndLng(p2long);
                DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                entry.setTimestamp(fmt.print(DateTime.now()));
                topRef.push().setValue(entry);

            }
            double Distance = DistanceCalculator.distance(p1Lat, p1Long, p2lat, p2long);   //calculate distance in km
            double Bearing = BearingCalculator.bearing(p1Lat, p1Long, p2lat, p2long);    //calculate bearing in deg

            DecimalFormat f = new DecimalFormat("#.##");
            if(distUnits.compareTo("Miles") == 0 ){
                Double distance= Distance * 0.621371;
                distanceResult.setText("Distance: " + f.format(distance) + " " + distUnits);
            }else{
                distanceResult.setText("Distance: " + f.format(Distance) + " " + distUnits);
            }
            if(bearUnits.compareTo("Mils") == 0 ){
                Double bearing= Bearing * 17.777777777778;
                bearingResult.setText("Bearing: " + f.format(bearing) + " " + bearUnits);
            }else{
                bearingResult.setText("Bearing: " + f.format(Bearing) + " " + bearUnits);
            }
        }
    }

    private ChildEventListener chEvListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            LocationLookup entry = (LocationLookup) dataSnapshot.getValue(LocationLookup.class);
            entry._key = dataSnapshot.getKey();
            allHistory.add(entry);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            LocationLookup entry = (LocationLookup) dataSnapshot.getValue(LocationLookup.class);
            List<LocationLookup> newHistory = new ArrayList<LocationLookup>();
            for (LocationLookup t : allHistory) {
                if (!t._key.equals(dataSnapshot.getKey())) {
                    newHistory.add(t);
                }
            }
            allHistory = newHistory;
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}


