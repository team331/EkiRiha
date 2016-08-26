package com.enpit.t311.ekiriha;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

public class EkiActivity extends AppCompatActivity {

    private int station = -1;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eki);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_station);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    station = i;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent){
                    //// TODO: 16/8/23
                }

            });
        }

        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(station == 0) {
                    Intent it = new Intent();
                    it.setClass(context, RoutingActivity.class);
                    it.putExtra("station_pos", station);
                    context.startActivity(it);
                }else{
                    Function.Toast(context, getString(R.string.unsupport));
                }
            }
        });


    }
}
