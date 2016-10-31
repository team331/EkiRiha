package com.enpit.t331.ekirihatsukuba;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    public Activity mActivity = this;
    private CustomPopWindow mCustomWindow;
    private PopupWindow mPopupWindow;
    private boolean firstEnter = true;

    private LatLng startLatLng, homeLatLng;
    private View mAppBar;
    private CalTitudeList ctl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mAppBar = findViewById(R.id.appBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                mPopupWindow.showAsDropDown(view, 0, -400);
                mMap.clear();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ctl = new CalTitudeList(mActivity, getspotId());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public int getspotId(){
        return 2;
    }
    private boolean needShowIntroduce(){
        return true;
    }

    private boolean needShowSetting(){
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(firstEnter) {
            firstEnter = false;
            if (needShowIntroduce()) {
                mCustomWindow = new CustomPopWindow(mActivity, new View.OnClickListener() {
                    public void onClick(View v) {
                        mCustomWindow.dismiss();
                        mCustomWindow.backgroundAlpha(mActivity, 1f);
                        if (needShowSetting()) {
                            mCustomWindow = new CustomPopWindow(mActivity, new View.OnClickListener() {
                                public void onClick(View v) {
                                    mCustomWindow.dismiss();
                                    mCustomWindow.backgroundAlpha(mActivity, 1f);

//                                ctl = new CalTitudeList(mActivity, getspotId());
                                }
                            }, "setting");
                            mCustomWindow.showAtLocation(findViewById(R.id.map), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        }
                    }
                }, "introduce");
                mCustomWindow.showAtLocation(findViewById(R.id.map), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            } else {
                if (needShowSetting()) {
                    mCustomWindow = new CustomPopWindow(mActivity, new View.OnClickListener() {
                        public void onClick(View v) {
                            mCustomWindow.dismiss();
                            mCustomWindow.backgroundAlpha(mActivity, 1f);

//                                ctl = new CalTitudeList(mActivity, getspotId());
                        }
                    }, "setting");
                    mCustomWindow.showAtLocation(findViewById(R.id.map), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    mCustomWindow.showAtLocation(findViewById(R.id.map), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        }

    }
    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

        startLatLng = AddMarkers();
        homeLatLng = new LatLng(36.108705, 140.103983);
        if(startLatLng == null){
            startLatLng = homeLatLng;
        }
        mMap.addMarker(new MarkerOptions().position(homeLatLng).title("筑波大学").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        AddRouteLines();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String tag = marker.getTitle();
                LatLngPlus target = ctl.searchByTag(tag);
                if(target!= null) {
                    setPopupWindow(target);
                    mPopupWindow.showAsDropDown(mAppBar);
                }
                return false;
            }
        });
        mMap.setIndoorEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 19));

    }

    private LatLng AddMarkers(){
        if(ctl==null){
            return null;
        }
        LatLng station = ctl.getStation();
        mMap.addMarker(new MarkerOptions().position(station).title("Station").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        ArrayList<LatLngPlus> ends = ctl.getBuilds();
        boolean randomColor = false;
        if(ends.size() < 9)
            randomColor = true;
        int count = 1;
        for(LatLngPlus end : ends){
            mMap.addMarker(new MarkerOptions().position(end.latlng).title(end.tag).icon(BitmapDescriptorFactory.defaultMarker(30.0F*count)));
            if(randomColor){
                count++;
            }
        }
        return station;
    }

    private void AddRouteLines(){
        if(ctl == null){
            return;
        }
        ArrayList<PolylineOptions> plos = ctl.getAllLines();
        for(PolylineOptions po: plos)
            mMap.addPolyline(po);
    }

    private void setPopupWindow(LatLngPlus llp){

        View popupView = getLayoutInflater().inflate(R.layout.layout_popupwindow, null);
        TextView tv = (TextView) popupView.findViewById(R.id.popupwindow_textview);
        tv.setText(llp.tag);
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapsActivity.this, "Next Move", Toast.LENGTH_SHORT).show();
            }
        });
        mPopupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);


    }
}
