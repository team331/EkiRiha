package com.enpit.t331.ekiriha;

import android.content.Context;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Context context = this;
    private GoogleMap mMap;
    private int station_num;
    private int start;
    private int end;
    private double[] station_titudes;
    private CalTitudeList ctl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent it = getIntent();
        station_num = it.getIntExtra("station", -1);
        start = it.getIntExtra("start", -1);
        end = it.getIntExtra("end", -1);

        ctl = new CalTitudeList(context, station_num, start , end);
        ctl.SearchRoute();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        int identify = this.getResources().getIdentifier("station_titude", "array", this.getPackageName());
//        String titudes = getResources().getStringArray(identify)[station_num];
//        String[] titude = titudes.split(",");
//        int mark = 0;
//        station_titudes = new double[2];
//        for (String str : titude) {
//            station_titudes[mark] = Double.parseDouble(str);
//            mark++;
//        }

        Button button_movie = (Button) findViewById(R.id.button_movie);
        button_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Function.Toast(getApplicationContext(), "show movie");
                ArrayList<String> list = ctl.getMovieList();
                Intent it = new Intent();
//                it.setClass(getApplicationContext(), MovieActivity.class);
                it.putStringArrayListExtra("movie_list",list);
                context.startActivity(it);
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

        LatLng start = AddMarker();
        AddRouteLine();
//        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.setIndoorEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 19));
    }

    private LatLng AddMarker(){
        ArrayList<Double> titudes = ctl.getTitudes();
        int last = titudes.size()-2;
        LatLng start = new LatLng(titudes.get(0), titudes.get(1));
        LatLng end = new LatLng(titudes.get(last), titudes.get(last+1));
        mMap.addMarker(new MarkerOptions()
                .position(start).title("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.addMarker(new MarkerOptions()
                .position(end).title("End")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        return start;
    }
    private void AddRouteLine(){
        PolylineOptions po = new PolylineOptions();
        ArrayList<Double> titudes = ctl.getTitudes();

        for(int i=0;i<titudes.size()-1;i=i+2){
            double x = titudes.get(i);
            double y = titudes.get(i+1);

            po.add(new LatLng(x,y));

        }
        po.width(5).color(Color.RED);
        mMap.addPolyline(po);
    }
}
