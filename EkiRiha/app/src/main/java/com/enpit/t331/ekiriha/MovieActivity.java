package com.enpit.t331.ekiriha;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {

    private Context context = this;
    private int i = 0;
    private ArrayList<Integer> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent it = getIntent();
        ArrayList<String> list = it.getStringArrayListExtra("movie_list");
        movies = new ArrayList<>();
        for(String item : list){
            movies.add(this.getResources().getIdentifier(item, "raw", this.getPackageName()));
        }


        //ID取得
        final VideoView v = (VideoView) findViewById(R.id.v);
        Function.Toast(context, movies.get(i));
        System.out.println(movies.get(i));
        //動画指定
        v.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + movies.get(i)));
        i++;
        v.start();


        v.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(i == movies.size()) {
                    v.stopPlayback();
                    return;
                }
                else {
                    v.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies.get(i++)));
                    v.start();
                    //配列の最後で停止
                }
            }
        });


    }
}
