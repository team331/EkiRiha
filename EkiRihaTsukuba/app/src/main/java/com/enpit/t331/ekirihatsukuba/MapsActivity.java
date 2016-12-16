package com.enpit.t331.ekirihatsukuba;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import com.google.maps.android.kml.KmlLayer;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private KmlLayer busRouteLayer = null;
    private boolean isBusRouteShown = false;
    public Activity mActivity = this;
    private CustomPopWindow mCustomWindow;
    private PopupWindow mPopupWindowBus;
    private PopupWindow mPopupWindow;
    private SpotSelectDialog mSpotSelectDialog;
    private FloatingActionButton fab, fab_store;
    private boolean firstEnter = true;
    private boolean mapReady = false;

    private LatLng startLatLng, homeLatLng;
    private View mapView;
    private CalTitudeList ctl;
    private DataManager dataManager;
    private boolean isStoreShown = false;
    private ArrayList<Marker> store_markers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //perpare data
        dataManager = new DataManager(getSharedPreferences("setting", 0));


        mapView = findViewById(R.id.map);
        setPopupWindowBus();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(mPopupWindowBus.isShowing()){
                }else{
                    mPopupWindowBus.showAtLocation(fab, Gravity.START,800,650);
                }
            }
        });
        fab_store = (FloatingActionButton) findViewById(R.id.fab_store);
        fab_store.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                toggleStores();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(firstEnter) {
            firstEnter = false;
            showSetting();
        }

    }

    private void showSetting(){
        if(dataManager.getInteger("setting") != -1)
            return;

        mSpotSelectDialog = new SpotSelectDialog();
        mSpotSelectDialog.setCancelable(false);
        mSpotSelectDialog.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                dataManager.setInteger("setting", mSpotSelectDialog.getSpotId());
                ctl = new CalTitudeList(mActivity, mSpotSelectDialog.getSpotId());
                startLatLng = AddMarkers();
                AddRouteLines();
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String tag = marker.getTitle();
                        LatLngPlus target = ctl.searchByTag(tag);
                        if(target!= null) {
                            setPopupWindow(target);
                            mPopupWindow.showAtLocation(mapView, Gravity.BOTTOM,0,0);
                        }
                        return false;
                    }
                });
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 18));
            }
        });
        mSpotSelectDialog.show(getFragmentManager(),"dialog");
    }
    @Override
    protected void onPause(){
        super.onPause();
//        mCustomWindow.saveMemoData();
    }

    public int getspotId(){
        return dataManager.getInteger("setting");
    }

    private void ShowMemo(){
        mCustomWindow = new CustomPopWindow(mActivity, new View.OnClickListener(){
            public void onClick(View v){
                mCustomWindow.dismiss();
                mCustomWindow.backgroundAlpha(mActivity, 1f);
                mCustomWindow.saveMemoData();
            }
        }, "memo", dataManager);
        mCustomWindow.showAtLocation(findViewById(R.id.map), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0,0);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapReady = true;
        homeLatLng = new LatLng(36.108705, 140.103983);


        if(getspotId() != -1){
            ctl = new CalTitudeList(mActivity, getspotId());
            startLatLng = AddMarkers();
            AddRouteLines();
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String tag = marker.getTitle();
                    LatLngPlus target = ctl.searchByTag(tag);
                    if(target!= null) {
                        setPopupWindow(target);
                        mPopupWindow.showAtLocation(mapView, Gravity.BOTTOM,0,0);
                    }
                    return false;
                }
            });
        }else{
            startLatLng = homeLatLng;
        }

        addStores();

        mMap.addMarker(new MarkerOptions().position(homeLatLng).title("筑波大学").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

        mMap.setIndoorEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 18));
        addBus();

    }

    private void addBus(){
        try {
            busRouteLayer = new KmlLayer(mMap, R.raw.bus, getApplicationContext());
        }catch (IOException e){
            e.printStackTrace();
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }
    }
    private void addStores(){
        String[] store_name = this.getResources().getStringArray(R.array.store);
        String[] store_points = this.getResources().getStringArray(R.array.store_points);
        for(int i=0; i< store_points.length; i++){
            String[] points = store_points[i].split(",");
            LatLng ll = new LatLng(Double.parseDouble(points[0]), Double.parseDouble(points[1]));
            Marker marker = mMap.addMarker(new MarkerOptions().position(ll).title(store_name[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            marker.setVisible(isStoreShown);
            store_markers.add(marker);
        }
    }

    private void toggleStores(){
        isStoreShown = !isStoreShown;
        for(Marker m: store_markers){
            m.setVisible(isStoreShown);
        }
    }

    private LatLng AddMarkers(){
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
        ArrayList<PolylineOptions> plos = ctl.getAllLines();
        for(PolylineOptions po: plos)
            mMap.addPolyline(po);
    }

    private void setPopupWindow(final LatLngPlus llp){
        final String jpgAddrPrefix = "http://www.human.tsukuba.ac.jp/shien/map/img/detail";

        View popupView = getLayoutInflater().inflate(R.layout.layout_popupwindow, null);
        TextView tv = (TextView) popupView.findViewById(R.id.popupwindow_textview);
        tv.setText(llp.tag);

        TextView movie_link = (TextView) popupView.findViewById(R.id.text_movie);
        movie_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = ctl.getMovieList(llp.tag);
                for (String item: list) {
                    System.out.println(item);
                }
                Intent it = new Intent();
                it.setClass(getApplicationContext(), MovieActivity.class);
                it.putStringArrayListExtra("movie_list", list);
                mActivity.startActivity(it);
            }
        });

        final ImageView image = (ImageView) popupView.findViewById(R.id.floor_photo);
        if(!llp.tag.equals("Gym"))
            Picasso.with(mActivity).load(jpgAddrPrefix+llp.getFloorJPGName().get(0)).into(image);

        final ArrayList<TextView> textViewList = new ArrayList<>();
        for(int i=1; i<=6;i++) {
            int text_id = mActivity.getResources().getIdentifier("text_" + i + "F", "id", mActivity.getPackageName());
            textViewList.add((TextView) popupView.findViewById(text_id));
        }
        textViewList.get(0).setText(textViewList.get(0).getText()+"->");
        for(int i=1;i<=6;i++){
            if(i>llp.getFloorNumber()){
                textViewList.get(i-1).setVisibility(View.INVISIBLE);
                textViewList.get(i-1).setClickable(false);
            }else{
                final int num = i-1;
                textViewList.get(i-1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(llp.tag != "Gym")
                            Picasso.with(mActivity).load(jpgAddrPrefix+llp.getFloorJPGName().get(num)).into(image);
                        for(TextView tv: textViewList){
                            tv.setText(tv.getText().subSequence(0,2));
                        }
                        TextView thisview = (TextView) view;
                        thisview.setText(thisview.getText()+"->");
                    }
                });
            }
        }

        mPopupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
    }

    private void setPopupWindowBus(){
        final String guide_addr = "http://kantetsu.co.jp/bus/guide1.html";
        final String time_addr = "http://kantetsu.jorudan.biz/?p=d&sc=41616&pn=6&v=&b1=%E3%81%A4%E3%81%8F%E3%81%B0%E3%82%BB%E3%83%B3%E3%82%BF%E3%83%BC&m=b";

        View popupView = getLayoutInflater().inflate(R.layout.popupwindow_bus, null);
        TextView time = (TextView) popupView.findViewById(R.id.bus_time);
        TextView route = (TextView) popupView.findViewById(R.id.bus_route);
        TextView howto = (TextView) popupView.findViewById(R.id.bus_howto);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(time_addr);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                mActivity.startActivity(it);
            }
        });

        howto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(guide_addr);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                mActivity.startActivity(it);
            }
        });

        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(busRouteLayer !=null){
                    try {
                        if(isBusRouteShown){
                            busRouteLayer.removeLayerFromMap();
                            isBusRouteShown = false;
                        }else {
                            busRouteLayer.addLayerToMap();
                            isBusRouteShown = true;
                        }
                    }catch (XmlPullParserException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        mPopupWindowBus = new PopupWindow(popupView, 300, LayoutParams.WRAP_CONTENT, true);
        mPopupWindowBus.setTouchable(true);
        mPopupWindowBus.setOutsideTouchable(true);
        mPopupWindowBus.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindowBus.setAnimationStyle(R.style.anim_menu_bus);

    }

    //default settings

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

}
