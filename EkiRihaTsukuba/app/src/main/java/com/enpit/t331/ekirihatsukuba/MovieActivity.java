package com.enpit.t331.ekirihatsukuba;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;
import android.media.MediaPlayer;
import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, Runnable {

    private Context context = this;
    private int vnum = 0; //動画番号
    private int totalTime = 0; //総動画時間
    private int crPosition = 0; //現在時間位置
    private ArrayList<Integer> movies;
    private int nowTime = 0; //現在時間
    private boolean flag = true; //スレッド時間差回避用フラグ
    private boolean thFlag = true; //スレッド停止用フラグ
    private int vLen; //動画の要素数
    private int[] lapTime; //シークバーで使用するラップタイム

    private VideoView vd;
    private TextView tvcpm;
    private SeekBar skb;
    private Button bpp;
    private Thread crThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //ID取得
        vd = (VideoView) findViewById(R.id.vd);
        tvcpm = (TextView) findViewById(R.id.tvcpm);
        skb = (SeekBar) findViewById(R.id.skb);
        bpp = (Button) findViewById(R.id.bpp);

        thFlag = true;

        Intent it = getIntent();
        ArrayList<String> list = it.getStringArrayListExtra("movie_list");
        movies = new ArrayList<>();
        for(String item : list){
            if(MainActivity.DEBUG) System.out.println("Movie: " + item);
            movies.add(this.getResources().getIdentifier(item, "raw", this.getPackageName()));
        }
        vLen = movies.size(); //動画の要素数
        lapTime = new int[vLen+1]; //シークバーで使用するラップタイム
        lapTime[0] = 0;

        //総動画再生時間の取得
        for(int j=0; j<vLen; j++){
            MediaPlayer mp = MediaPlayer.create(this, movies.get(j));
            totalTime += mp.getDuration();
            lapTime[j+1] = lapTime[j] + mp.getDuration();
        }
        skb.setMax(totalTime);

        //スレッド開始
        crThread = new Thread(this);
        crThread.start();

        //ボタン
        bpp.setOnClickListener(this);

        //動画指定
        vd.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + movies.get(0)));
        vd.start();

        //シークバー
        skb.setOnSeekBarChangeListener(this);

        //動画終了時
        vd.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        flag = false;

        if (++vnum == movies.size()) {
            //強制終了を回避するために動画の先頭へ
            vnum = 0;
            flag = true;
            vd.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies.get(vnum)));
            vd.seekTo(vnum);
            skb.setProgress(vnum);
            bpp.setText(R.string.b_play);
            vd.pause();
            return;
        } else {
            vd.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies.get(vnum)));
            vd.start();
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bpp:
                if(vd.isPlaying()){
                    vd.pause();
                    bpp.setText(R.string.b_play);
                } else {
                    vd.start();
                    bpp.setText(R.string.b_pause);
                }
        }
    }

    @Override
    public void run(){
        while(vd != null && thFlag) {
            try {
                crPosition = vd.getCurrentPosition();
                Message msg = new Message();
                msg.what = crPosition;
                tHandler.sendMessage(msg);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler tHandler = new Handler() {
      public void handleMessage(Message msg) {
          //時間差回避
          if(flag == true)
              nowTime = lapTime[vnum];
          else
              nowTime = lapTime[vnum-1];
          if(msg.what >= 0)
              nowTime += msg.what;
          if(msg.what == 0)
              flag = true;

          skb.setSecondaryProgress(nowTime);
          tvcpm.setText(String.valueOf(nowTime/1000) + " / " + String.valueOf(totalTime/1000) + "[sec]");
      }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        for(int k = 0; k < vLen; k++)
            if (progress > lapTime[k] && progress <= lapTime[k + 1]) {
                //k番目の動画指定
                vd.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies.get(k)));
                vd.seekTo(progress - lapTime[k]);
                vnum = k;
            }
        vd.start();
        bpp.setText(R.string.b_pause);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        //Backキーの処理
        if(keyCode == KeyEvent.KEYCODE_BACK){
            thFlag = false;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}