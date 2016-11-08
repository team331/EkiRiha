package jp.a331.prototype3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    int i = 0;
    int j = 0;
    int totalTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ID取得
        final VideoView v = (VideoView) findViewById(R.id.v);
        final TextView tv1 = (TextView) findViewById(R.id.tv1);
        final TextView tv2 = (TextView) findViewById(R.id.tv2);
        final SeekBar skb = (SeekBar) findViewById(R.id.skb);

        final int[] movies = new int[]{R.raw.tsukuba_m_7_6, R.raw.tsukuba_m_6_3, R.raw.tsukuba_m_3_2, R.raw.tsukuba_m_2_1, R.raw.tsukuba_m_1_0, 0};
        final int vLen = movies.length; //動画の要素数
        final int[] lapTime = new int[vLen]; //シークバーで使用するラップタイム

        //総動画再生時間の取得
        while (movies[j] != 0) {
            MediaPlayer mp = MediaPlayer.create(this, movies[j]);
            totalTime += mp.getDuration();
            lapTime[j+1] = lapTime[j] + mp.getDuration();
            j++;
        }

        //総動画再生時間の出力
        tv1.setText("TotalTime:" +String.valueOf(totalTime) + "msec");
        skb.setMax(totalTime);

        //動画指定
        v.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + movies[0]));
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
                                v.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies[k]));
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
                v.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + movies[++i]));
                v.start();
                //配列の最後で停止
                if(movies[i] == 0)
                    v.stopPlayback();
            }
        });

    }
}

