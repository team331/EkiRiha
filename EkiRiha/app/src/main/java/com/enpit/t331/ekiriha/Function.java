package com.enpit.t331.ekiriha;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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


    public void CalLinkList(Context context, int station_pos , int start_id , int end_id){
        String station = context.getResources().getStringArray(R.array.station_mark)[station_pos];
        String station_select_map = station + "_select_map";
        int identify = context.getResources().getIdentifier(station_select_map, "array", context.getPackageName());

        String[] mItems = context.getResources().getStringArray(identify);
        int start_map_id = Integer.parseInt(mItems[start_id]);
        int end_map_id = Integer.parseInt(mItems[end_id]);

        String station_links = station + "_links";
        identify = context.getResources().getIdentifier(station_links, "array", context.getPackageName());
        String[] links = context.getResources().getStringArray(identify);

        Map<Integer,Integer> map = new HashMap<Integer, Integer>();

        for(String link : links) {
            String[] link_pair_s = link.split(",");
            int[] link_pair = new int[2];
            link_pair[0] = Integer.parseInt(link_pair_s[0]);
            link_pair[1] = Integer.parseInt(link_pair_s[1]);
            map.put(link_pair[0], link_pair[1]);
            map.put(link_pair[1], link_pair[0]);
        }
    }

}
