package com.example.geocalc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationSearchActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE_1 = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_2 = 2;
    private static final String TAG = "LocationSearchActivity";

    @BindView(R.id.location1) TextView location1;
    @BindView(R.id.location2) TextView location2;
    @BindView(R.id.date) TextView dateView;

    private DateTime date;
    private DatePickerDialog dpDialog;
    private LocationLookup locationLookup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        ButterKnife.bind(this);

        locationLookup = new LocationLookup();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DateTime today = DateTime.now();
        dpDialog = DatePickerDialog.newInstance(this,
                today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());


        dateView.setText(formatted(today));
        date = today;
    }

    @OnClick(R.id.location1)
    public void location1Pressed() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG);
        Intent intent =
                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_1);
    }

    @OnClick(R.id.location2)
    public void location2Pressed() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG);
        Intent intent =
                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_2);
    }

    @OnClick(R.id.date)
    public void datePressed() {
        dpDialog.show(getFragmentManager(), "daterangedialog");
    }

    @OnClick(R.id.fab)
    public void FABPressed() {
        Intent result = new Intent();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        locationLookup.startDate = fmt.print(startDate);
        currentTrip.endDate = fmt.print(endDate);
        locationLookup.origLat = location1.
        // add more code to initialize the rest of the fields
        Parcelable parcel = Parcels.wrap(currentTrip);
        result.putExtra("TRIP", parcel);
        setResult(RESULT_OK, result);
        finish();
    }

    private String formatted(DateTime d) {
        return d.monthOfYear().getAsShortText(Locale.getDefault()) + " " +
                d.getDayOfMonth() + ", " + d.getYear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_1) {
            if (resultCode == RESULT_OK) {
                Place pl = Autocomplete.getPlaceFromIntent(data);
                location1.setText(pl.getName());
                currentTrip.location = pl.getName();
                currentTrip.lat = pl.getLatLng().latitude;
                currentTrip.lng = pl.getLatLng().longitude;
                currentTrip.placeId = pl.getId();

                Log.i(TAG, "onActivityResult: " + pl.getName() + "/" + pl.getAddress());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status stat = Autocomplete.getStatusFromIntent(data);
                Log.d(TAG, "onActivityResult: ");
            } else if (requestCode == RESULT_CANCELED) {
                System.out.println("Cancelled by the user");
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = new DateTime(year, month + 1, dayOfMonth, 0, 0);
        dateView.setText(formatted(date));
    }
}
