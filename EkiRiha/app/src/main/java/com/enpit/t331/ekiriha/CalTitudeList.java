package com.enpit.t331.ekiriha;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by woo on 16/8/26.
 */
public class CalTitudeList {
/*todo
    station id -> station_name -> station_select ->station_select_map
    start end -> station_select_map
    [station]_links -> link
    link -> [station_points]
 */
    private Context context;
    private int station_pos, start_id, end_id;

    private int start_map_id , end_map_id;

    private String station;
    private Map<Integer, ArrayList<Integer>> map;

    private ArrayList<Integer> visited;

    private ArrayList<Integer> route;


    CalTitudeList(Context context, int station_pos , int start_id , int end_id) {
        this.context = context;
        this.start_id = start_id;
        this.end_id = end_id;
        CreateGraph();
    }

    private void CreateGraph(){
        station = context.getResources().getStringArray(R.array.station_mark)[station_pos];
        String station_select_map = station + "_select_map";
        int identify = context.getResources().getIdentifier(station_select_map, "array", context.getPackageName());

        String[] mItems = context.getResources().getStringArray(identify);
        start_map_id = Integer.parseInt(mItems[start_id]);
        end_map_id = Integer.parseInt(mItems[end_id]);

        String station_links = station + "_links";
        identify = context.getResources().getIdentifier(station_links, "array", context.getPackageName());
        String[] links = context.getResources().getStringArray(identify);

        map = new HashMap<Integer, ArrayList<Integer>>();

        for(String link : links) {
            String[] link_pair_s = link.split(",");
            int[] link_pair = new int[2];
            link_pair[0] = Integer.parseInt(link_pair_s[0]);
            link_pair[1] = Integer.parseInt(link_pair_s[1]);
            addHashMapItem(link_pair[0], link_pair[1]);
            addHashMapItem(link_pair[1], link_pair[0]);
        }
    }

    private void addHashMapItem(int key , int value){
        if(map.get(key) == null){
            ArrayList<Integer> list = new ArrayList<>();
            list.add(value);
            map.put(key, list);
        }else{
            ArrayList list = map.get(key);
            list.add(value);
            map.put(key, list);
        }
    }


    public void SearchRoute(){
        //type: 0 depth first, 1 width first
        visited = new ArrayList<Integer>();
        String routing = "" + start_map_id;
        SearchRouteWidth(start_map_id,routing);
    }

    private void SearchRouteWidth(int now_pos, String routing){
        visited.add(now_pos);
        ArrayList list = map.get(now_pos);
        if(list != null) {
            if (list.contains(end_map_id) && !visited.contains(end_map_id)) {
                visited.add(end_map_id);
                routing = routing + "," + end_map_id;
                convertRoute(routing);
            }
            for (Object i : list) {
                int next = (int) i;
                if (!visited.contains(next)) {
                    visited.add(next);
                    SearchRouteWidth(next, routing + "," + next);
                }
            }
        }
    }

    private void convertRoute(String routing){
        String route_node[] = routing.split(",");
        route = new ArrayList<>();
        for (String node: route_node) {
            route.add(Integer.parseInt(node));
        }
    }

    public ArrayList<Double> getTitudes(){
        String station_points = station + "_points";
        int identify = context.getResources().getIdentifier(station_points, "array", context.getPackageName());

        String[] mItems = context.getResources().getStringArray(identify);

        ArrayList<Double> titudes_list = new ArrayList<>();
        for(int point : route){
            String[] titude = mItems[point].split(",");
            titudes_list.add(Double.parseDouble(titude[0]));
            titudes_list.add(Double.parseDouble(titude[1]));
        }

        return titudes_list;
    }
}
