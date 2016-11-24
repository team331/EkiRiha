package com.enpit.t331.ekirihatsukuba;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {

    private Context context = this;
    private int i = 0;
    private int j = 0;
    private int totalTime = 0;
    private ArrayList<Integer> movies;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //ID取得
        final VideoView v = (VideoView) findViewById(R.id.v);
        final TextView tv1 = (TextView) findViewById(R.id.tv1);
        final TextView tv2 = (TextView) findViewById(R.id.tv2);
        final SeekBar skb = (SeekBar) findViewById(R.id.skb);

        Intent it = getIntent();
        ArrayList<String> list = it.getStringArrayListExtra("movie_list");
        movies = new ArrayList<>();
        for(String item : list){
            movies.add(this.getResources().getIdentifier(item, "raw", this.getPackageName()));
        }
        final int vLen = movies.size(); //動画の要素数
        final int[] lapTime = new int[vLen+1]; //シークバーで使用するラップタイム
        lapTime[0] = 0;

        //総動画再生時間の取得
        for(int j=0; j<vLen; j++){
            MediaPlayer mp = MediaPlayer.create(this, movies.get(j));
            totalTime += mp.getDuration();
            lapTime[j+1] = lapTime[j] + mp.getDuration();
        }

        //総動画再生時間の出力
        tv1.setText("TotalTime:" +String.valueOf(totalTime) + "msec");
        skb.setMax(totalTime);

        //動画指定
        v.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + movies.get(0)));
        v.start();

        //シークバー
        skb.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //現在時間の出力
                        tv2.setText("CurrentTime:" + String.valueOf(progress) + "msec");

                        for(int k = 0; k < vLen-1; k++)
                            if (progress > lapTime[k] && progress <= lapTime[k + 1]) {
                                //k番目の動画指定
                                v.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies.get(k)));
                                v.seekTo(progress - lapTime[k]);
                                i = k;
                            }
                        v.start();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        v.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(i == movies.size()) {
                    v.stopPlayback();
                    return;
                }
                v.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies.get(++i)));
                v.start();
                //配列の最後で停止

            }
        });

    }
}
