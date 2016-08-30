package jp.a331.prototype3;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;
import android.net.Uri;

public class MainActivity extends AppCompatActivity {
    private Context context = this;
    int i = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ID取得
        final VideoView v = (VideoView) findViewById(R.id.v);

        final int[] movies = new int[]{R.raw.tsukuba_m_0_1, R.raw.tsukuba_m_1_2, R.raw.tsukuba_m_2_3, R.raw.tsukuba_m_3_6, R.raw.tsukuba_m_6_5, R.raw.tsukuba_m_5_4, 0};

        //動画指定
        v.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + movies[0]));
        v.start();


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
