package com.example.bayrammuradovmustafa.crocusofttask4;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    GoogleMap gMap;
    MapView mapView;

    int clickCounter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        boolean start = false;
        init(this);
    }

    private void init(Context cntx) {
        //getting xml elements
        ImageButton btnEnter = (ImageButton) findViewById(R.id.img_bttn_enter);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(WelcomeActivity.this);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText latitude = findViewById(R.id.et_latitude);
                EditText longitude = findViewById(R.id.et_longitude);
                //in case, fields are empty warn user
                if(latitude.getText().toString().equals("")|| latitude.getText().toString().equals("")) {
                    Toast.makeText(WelcomeActivity.this, "field is empty", Toast.LENGTH_SHORT).show();
                }
                //else, get values&mark location
                else {
                    final double latitude_var = Double.parseDouble(latitude.getText().toString());
                    final double longitude_var = Double.parseDouble(longitude.getText().toString());
                    LatLng myLatLng = new LatLng(latitude_var, longitude_var);
                    String address = getAdress(WelcomeActivity.this, latitude_var, longitude_var);
                    gMap.addMarker(new MarkerOptions().position(myLatLng).title(address));
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatLng.latitude, myLatLng.longitude), 9.0f));
                    Log.e("pressed", "true");
                }
            }
        });
    }

    public boolean isOk() {
        EditText latitude = findViewById(R.id.et_latitude);
        EditText longitude = findViewById(R.id.et_longitude);
        if (!(latitude.getText().toString().equals("") || longitude.getText().toString().equals(""))) {
            return true;
        }
        return false;
    }

    public String getAdress(Context cntx, double lat, double lng) {
        String fullAddr = null;
        try {
            Geocoder geocoder = new Geocoder(cntx, Locale.getDefault());
            List<Address> adresses = geocoder.getFromLocation(lat, lng, 1);
            if (adresses.size() > 0) {
                Address address = adresses.get(0);
                fullAddr = address.getAddressLine(0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fullAddr;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMapClickListener(this);
        Log.e("map","ready");
    }

    double latit=0;
    double longit=0;
    double latit2=0;
    double longit2=0;
    @Override
    public void onMapClick(LatLng latLng) {
        Log.i("clicked", "on map");

        clickCounter++;
        if(clickCounter==1) {
            System.out.println("pressed 1st time");
            latit=latLng.latitude;
            longit=latLng.longitude;
            System.out.println("lat1:" + latit + "\tlng1:" + longit);
        }

        if(clickCounter==2) {
            System.out.println("pressed 2nd time");
            latit2=latLng.latitude;
            longit2=latLng.longitude;
            System.out.println("lat1------------lon1---------");
            System.out.println("lat1:" + latit + "\tlng1:" + longit);
            System.out.println("lat1------------lon1---------");
            System.out.println("lat2:"+ latit2+"\tlng2"+longit2);
            meterDistanceBetweenPoints(latit, longit, latit2, longit2);
        }

    }

    public void updateDistanceText(float inp) {
        String newDist = Float.toString(inp);
        TextView distText = findViewById(R.id.tv_distance);
        distText.setText(newDist);
    }

    private void meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        float pk = (float) (180.f/Math.PI);

        float a1 = (float)lat_a / pk;
        float a2 = (float) lng_a / pk;
        float b1 = (float) lat_b / pk;
        float b2 = (float)lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        float input = (float) (6366000 * tt);
        updateDistanceText(input);
    }
}


