package com.enpit.t331.ekiriha;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int station_num;
    private double[] station_titudes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent it = getIntent();
        station_num = it.getIntExtra("station", -1);
        int identify = this.getResources().getIdentifier("station_titude", "array", this.getPackageName());
        String titudes = getResources().getStringArray(identify)[station_num];
        String[] titude = titudes.split(",");
        int mark = 0;
        station_titudes = new double[2];
        for (String str : titude) {
            station_titudes[mark] = Double.parseDouble(str);
            mark++;
        }

        Button button_movie = (Button) findViewById(R.id.button_movie);
        button_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Function.Toast(getApplicationContext(), "show movie");
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng station = new LatLng(station_titudes[0], station_titudes[1]);
        mMap.addMarker(new MarkerOptions().position(station).title("Start"));

//        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.setIndoorEnabled(true);
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(36.082812, 140.111431), new LatLng(36.082857, 140.111553))
                .add(new LatLng(36.082664, 140.111762))
                .width(5)
                .color(Color.RED));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(station));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
    }

    public void getPolyLine(){

    }
}
