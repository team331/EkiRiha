package com.enpit.t331.ekirihatsukuba;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//起動スクリン及びユーザーデータを読む
public class MainActivity extends AppCompatActivity {
    private Context m_context = this;
    private static final int MIN_SHOW_TIME = 800;//ms
    public static final boolean DEBUG = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        new AsyncTask<Void, Void, Integer>(){

            @Override
            protected Integer doInBackground(Void... params){
                int result=0;
                long startTime = System.currentTimeMillis();
                //todo: load data
                long loadingTime = System.currentTimeMillis() - startTime;
                if(loadingTime < MIN_SHOW_TIME){
                    try{
                        Thread.sleep(MIN_SHOW_TIME - loadingTime);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

                return result;
            }

            @Override
            protected void onPostExecute(Integer result){
                Intent it = new Intent();
                it.setClass(m_context, MapsActivity.class);
                startActivity(it);
            };
        }.execute(new Void[]{});
    }
}
