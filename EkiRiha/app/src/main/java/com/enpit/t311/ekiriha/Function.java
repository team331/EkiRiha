package com.enpit.t311.ekiriha;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by woo on 16/8/26.
 */
public class Function {
    public static void Toast(Context ct , String text){
        Toast.makeText(ct, text , Toast.LENGTH_SHORT).show();
    }

    public static void Toast(Context ct, String text, int time){
        Toast(ct, text, time);
    }
    public static void Toast(Context ct , int text){
        String str = ct.getString(text);
        if(str != null)
            Toast.makeText(ct, str , Toast.LENGTH_SHORT).show();
    }

}
