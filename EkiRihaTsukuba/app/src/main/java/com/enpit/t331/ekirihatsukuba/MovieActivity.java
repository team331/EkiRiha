package com.enpit.t331.ekirihatsukuba;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;
import android.media.MediaPlayer;
import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity implements Runnable {

    private Context context = this;
    private int i = 0;
    private int j = 0;
    private int totalTime = 0;
    private int crPosition = 0;
    private ArrayList<Integer> movies;
    private int nowTime = 0;
    private boolean flag = true;

    private VideoView vd;
    private TextView tvcpm;
    private SeekBar skb;
    private Button bpp;
    private Thread crThread;

    private int vLen;
    private int[] lapTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //ID取得
        vd = (VideoView) findViewById(R.id.vd);
        tvcpm = (TextView) findViewById(R.id.tvcpm);
        skb = (SeekBar) findViewById(R.id.skb);
        bpp = (Button) findViewById(R.id.bpp);

        Intent it = getIntent();
        ArrayList<String> list = it.getStringArrayListExtra("movie_list");
        movies = new ArrayList<>();
        for(String item : list){
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

        bpp.setOnClickListener(
                new Button.OnClickListener() {
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
                }
        );

        //動画指定
        vd.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + movies.get(0)));
        vd.start();

        //シークバー
        skb.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //現在時間の出力
                        tvcpm.setText(String.valueOf(progress/1000) + " / " + String.valueOf(totalTime/1000) + "[sec]");

                        for(int k = 0; k < vLen-1; k++)
                            if (progress > lapTime[k] && progress <= lapTime[k + 1]) {
                                //k番目の動画指定
                                vd.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies.get(k)));
                                vd.seekTo(progress - lapTime[k]);
                                i = k;
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
                }
        );

        vd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                flag = false;

                if(++i == movies.size()) {
                    //強制終了を回避するために動画の先頭へ
                    i=0;
                    flag = true;
                    vd.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies.get(0)));
                    vd.seekTo(0);
                    skb.setProgress(0);
                    bpp.setText(R.string.b_play);
                    vd.pause();
                    return;
                } else {
                    vd.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies.get(i)));
                    vd.start();
                }
            }
        });
    }

    @Override
    synchronized public void run(){
        try{
            while(vd != null) {
                crPosition = vd.getCurrentPosition();
                Message msg = new Message();
                msg.what = crPosition;
                tHandler.sendMessage(msg);
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Handler tHandler = new Handler() {
      public void handleMessage(Message msg) {
          if(flag == true)
              nowTime = msg.what + lapTime[i];
          else
              nowTime = msg.what + lapTime[i-1];
          if(msg.what == 0)
              flag = true;
          tvcpm.setText(nowTime/1000 + " / " + totalTime/1000 + "[sec]");
      }
    };
}