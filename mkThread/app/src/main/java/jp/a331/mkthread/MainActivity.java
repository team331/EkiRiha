package jp.a331.mkthread;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.os.Message;
import android.widget.TextView;
import android.widget.VideoView;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, Runnable {
    private Context context = this;
    int i = 0;
    int j = 0;
    int jk = 0;
    int totalTime = 0;
    int crTime = 0;
    boolean chTime = true;

    final int[] movies = new int[]{R.raw.tsukuba_m_7_6, R.raw.tsukuba_m_6_3, R.raw.tsukuba_m_3_2, R.raw.tsukuba_m_2_1, R.raw.tsukuba_m_1_0, 0};
    final int vLen = movies.length;
    final int[] lapTime = new int[vLen];
    final int[] vTime = new int[vLen];

    private MediaPlayer mp;
    private VideoView vd;
    private SeekBar skb;
    private SeekBar skb2;
    private TextView tvcpm;
    private TextView tvt;
    private TextView tvs;
    private Button bpp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vd = (VideoView) findViewById(R.id.vd);
        tvcpm = (TextView) findViewById(R.id.tvcpm);
        tvt = (TextView) findViewById(R.id.tvt);
        tvs = (TextView) findViewById(R.id.tvs);
        skb = (SeekBar) findViewById(R.id.skb);
        skb2 = (SeekBar) findViewById(R.id.skb2);
        bpp = (Button) findViewById(R.id.bpp);

        Thread crThread = new Thread(this);
        crThread.start();

        while (movies[j] != 0) {
            mp = MediaPlayer.create(this, movies[j]);
            totalTime += mp.getDuration();
            vTime[j] = mp.getDuration();
            lapTime[j+1] = lapTime[j] + mp.getDuration();
            j++;
        }

        //tvm.setText(String.valueOf(totalTime/1000) + "sec");
        skb.setMax(totalTime);
        skb.setProgress(0);
        skb2.setMax(totalTime/1000);
        skb2.setProgress(0);

        bpp.setOnClickListener(this);
        vd.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + movies[0]));
        vd.start();
        vd.setOnCompletionListener(this);
        skb.setOnSeekBarChangeListener(this);


        /*
        (new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    Log.d("test", "thread");
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        })).start();
        */
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
    public void onCompletion(MediaPlayer mediaPlayer) {
        vd.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + movies[++i]));
        //vd.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + movies[i]));
        vd.start();
        jk++;
        chTime = false;
        if (movies[i] == 0)
            vd.stopPlayback();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //tvt.setText(String.valueOf(progress) + "msec");
        //if (fromUser) {
            for (int k = 0; k < vLen - 1; k++)
                if (progress > lapTime[k] && progress <= lapTime[k+1]) {
                    vd.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + movies[k]));
                    vd.seekTo(progress - lapTime[k]);
                    //vd.seekTo((progress - lapTime[k])/1000);
                    //skb2.setProgress(progress/1000);
                    //skb.setProgress(progress);
                    i = k;
                }
            vd.start();
            bpp.setText(R.string.b_pause);
        //}
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void run(){
        try{
            while (vd != null) {
                //int crPosition = mp.getCurrentPosition();
                int crPosition = vd.getCurrentPosition(); //152
                Message msg = new Message();
                msg.what = crPosition;
                tHandler.sendMessage(msg);
            }
            Thread.sleep(100);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private Handler tHandler = new Handler() {
        public void handleMessage(Message msg) {
            //if(msg.what == 0)
            if(chTime == true)
                crTime = msg.what + lapTime[i];
            else
                crTime = msg.what + lapTime[i-1];
            if(msg.what == 0)
                chTime = true;
            skb2.setProgress(crTime/1000);
            //skb.setProgress(crTime);
            tvcpm.setText(String.valueOf(crTime/1000) + "sec / " + String.valueOf(totalTime/1000) + "sec");
            tvs.setText(String.valueOf(crTime) + "msec");
            //Log.d("test", String.valueOf(crTime) + " = m:" + String.valueOf(msg.what) + " + l:" + String.valueOf(lapTime[i]) + ":[" + String.valueOf(jk) + "]" + String.valueOf(vTime[i]));
        }
    };
}
