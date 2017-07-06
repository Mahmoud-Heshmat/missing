package com.example.mahmoudheshmat.missing.Activites;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.mahmoudheshmat.missing.utils.GPSTracker;
import com.example.mahmoudheshmat.missing.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String longitude;
    String latitude;

    String myLongitude;
    String myLatitude;

    LatLng lastLocation;
    LatLng mLocation;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Map");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back_inverted); // your drawable
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            longitude = extra.getString("longitude");
            latitude = extra.getString("latitude");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        myLongitude = String.valueOf(gpsTracker.latitude);
        myLatitude = String.valueOf(gpsTracker.longitude);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        lastLocation = new LatLng(Double.valueOf(longitude), Double.valueOf(latitude));

        mLocation = new LatLng(Double.valueOf(myLongitude), Double.valueOf(myLatitude));

        Marker marker = mMap.addMarker(new MarkerOptions().position(lastLocation).title("Marker").snippet("Last Seen location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 18));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(true); // if place support 3d buildings
        //mMap.setMyLocationEnabled(true); // Button to get my location
        mMap.getUiSettings().setZoomControlsEnabled(true); // Zoom
        mMap.getUiSettings().setAllGesturesEnabled(true); // gestures of map

        marker.showInfoWindow(); // Display all information about map marker



        // draw line between longitude and latitude
        /*Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(lastLocation, mLocation)
                .color(Color.RED)
                .width(15)
                .geodesic(true));*/

    }

    public void connect(View view){
        GoogleDirection.withServerKey("AIzaSyA2b4rO8TSjSZZ2sN0SKGSGn-kGqkCIuBY")
                .from(mLocation)
                .to(lastLocation)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()){
                            Leg leg = direction.getRouteList().get(0).getLegList().get(0);
                            ArrayList<LatLng> directionPostionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(MapsActivity.this, directionPostionList,
                                    5, Color.RED);

                            mMap.addPolyline(polylineOptions);
                            Log.d("Direction", "Success");
                        }else{
                            Log.d("Direction", direction.getErrorMessage());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.d("Direction", "Failure");
                    }
                });

    }
}
