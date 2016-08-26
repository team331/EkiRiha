package com.enpit.t331.ekiriha;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class RoutingActivity extends AppCompatActivity {

    private Context mContext = this;
    private int station_pos;
    private int route_start = -1;
    private int route_end = -2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routing);
        Intent it = getIntent();
        //station position
        station_pos = it.getIntExtra("station_pos", -1);
        //station name
        String station = getResources().getStringArray(R.array.station_mark)[station_pos];
        int identify = this.getResources().getIdentifier(station, "array", this.getPackageName());
        //routing names of this station
        String[] mItems = getResources().getStringArray(identify);

        Spinner spinner_start = (Spinner) findViewById(R.id.spinner_start);
        Spinner spinner_end   = (Spinner) findViewById(R.id.spinner_end);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item);
        for(int i=0; i < mItems.length; i++){
            mAdapter.add(mItems[i]);
        }
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_start.setAdapter(mAdapter);
        spinner_end.setAdapter(mAdapter);

        spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                route_start = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                route_end = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button button_search = (Button) findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(route_end == route_start){
                    Function.Toast(getApplicationContext(), R.string.warning_start_end_same);
                }else {
                    Intent it = new Intent();
                    it.setClass(getApplicationContext(), MapsActivity.class);
                    it.putExtra("station", station_pos);
                    it.putExtra("start", route_start);
                    it.putExtra("end", route_end);
                    mContext.startActivity(it);
                }
            }
        });


    }
}
