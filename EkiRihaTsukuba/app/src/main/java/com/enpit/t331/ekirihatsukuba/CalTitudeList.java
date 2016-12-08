package com.enpit.t331.ekirihatsukuba;

/**
 * Created by woo on 16/10/25.
 */
import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by woo on 16/8/26.
 */
public class CalTitudeList {
    private Context context;
    private int spot_id, build_id;

    private ArrayList<LatLngPlus> points = new ArrayList<>();
    private String spot_name;

    private ArrayList<Integer> visited = new ArrayList<>();
    private ArrayList<Integer> poor = new ArrayList<>();
    private ArrayList<Integer> short_line = new ArrayList<>();
    private PolylineOptions po;
    private int now;


    CalTitudeList(Context context, int spotID) {
        this.context = context;
        this.spot_id = spotID;

        CreateGraph();
    }

//    CalTitudeList(Context context, int spotID, int buildID){
//        this.context = context;
//        this.spot_id = spotID;
//        this.build_id = buildID;
//        CreateGraph();
//    }

    private void CreateGraph(){
        spot_name = context.getResources().getStringArray(R.array.spot_code)[spot_id];

        String spot_points_str = spot_name + "_points";
        String[] spot_points_latlng_str = context.getResources().getStringArray(context.getResources().getIdentifier(spot_points_str, "array", context.getPackageName()));
        for(int i=0; i<spot_points_latlng_str.length; i++){
            String[] spot_latlng = spot_points_latlng_str[i].split(",");
            LatLngPlus llp = new LatLngPlus(Double.parseDouble(spot_latlng[0]), Double.parseDouble(spot_latlng[1]) , i);
            points.add(llp);
        }

        String spot_select_map = spot_name + "_select_map";
        int identify = context.getResources().getIdentifier(spot_select_map, "array", context.getPackageName());
        String[] electable_spot_ids_string = context.getResources().getStringArray(identify);


        String[] electable_spot_tag_str = context.getResources().getStringArray(context.getResources().getIdentifier(spot_name+"_select_tag", "array", context.getPackageName()));
        String[] electable_spot_floor_num = context.getResources().getStringArray(context.getResources().getIdentifier(spot_name+"_select_floor", "array", context.getPackageName()));
        for(int i=0;i<electable_spot_ids_string.length;i++){
            points.get(Integer.parseInt(electable_spot_ids_string[i])).setIsBuild();
            points.get(Integer.parseInt(electable_spot_ids_string[i])).tag = electable_spot_tag_str[i];
            points.get(Integer.parseInt(electable_spot_ids_string[i])).setFloorNumber(Integer.parseInt(electable_spot_floor_num[i]));
        }
        points.get(0).setIsStation();

        String spot_links = spot_name + "_links";
        identify = context.getResources().getIdentifier(spot_links, "array", context.getPackageName());
        String[] links = context.getResources().getStringArray(identify);

        for(String link : links) {
            String[] link_pair_s = link.split(",");
            int a,b;
            a = Integer.parseInt(link_pair_s[0]);
            b = Integer.parseInt(link_pair_s[1]);
            points.get(a).addNeighbor(b);
            points.get(b).addNeighbor(a);
        }
    }

    public LatLng getStation(){
        for (LatLngPlus llp: points) {
            if(llp.isStation())
                return llp.latlng;
        }
        return null;
    }

    public ArrayList<LatLngPlus> getBuilds(){
        ArrayList<LatLngPlus> lls = new ArrayList<>();
        for(LatLngPlus llp: points){
            if(llp.isBuild()){
                lls.add(llp);
            }
        }
        return lls;
    }

    public ArrayList<PolylineOptions> getAllLines(){
        ArrayList<PolylineOptions> plos = new ArrayList<>();
        po = new PolylineOptions();
        po.width(5).color(Color.RED);
        now = 0;
        poor.clear();
        visited.clear();
        while(visited.size() < points.size()) {
            plos = searchLoop(plos);
        }
        return plos;
    }

    private ArrayList<PolylineOptions> searchLoop(ArrayList<PolylineOptions> plos){
        boolean flag = true;
        if(plos==null) flag = false;
        if(flag) po.add(points.get(now).latlng);
        if(!visited.contains(now))
            visited.add(now);
        if(!flag) short_line.add(now);
        ArrayList<Integer> unVisitedNeighbor = points.get(now).getUnVisitedNeighbor(visited);
        if(unVisitedNeighbor.size() > 0){
            if(unVisitedNeighbor.size() > 1){
                poor.add(now);
            }
            now = unVisitedNeighbor.get(0);
        }else{
            if(flag) plos.add(po);
            if(poor.size()>0) {
                if(flag) {
                    po = new PolylineOptions();
                    po.width(5).color(Color.RED);
                }
                now = poor.remove(0);
                if(!flag) {
                    int index = short_line.indexOf(now);
                    short_line.subList(index, short_line.size()).clear();
                }
            }
        }
        return plos;
    }
    public LatLngPlus searchByTag(String tag){
        for(LatLngPlus item: points){
            if(tag.equals(item.tag)){
                return item;
            }
        }
        return null;
    }

    public ArrayList<String> getMovieList(String tag){
        //must be used after convertRoute
        ArrayList<String> list = new ArrayList<>();
        now = 0;
        poor.clear();
        visited.clear();
        short_line.clear();
        while(points.get(now).tag != tag){
            searchLoop(null);
        }
        short_line.add(now);
        int pre = 0;
        for(int i=0;i<short_line.size(); i++){
            if(points.get(short_line.get(i)).isBranchOrBuild()) {
                String tmp = spot_name + "_m_" + pre + "_" + short_line.get(i);
                list.add(tmp);
                pre = short_line.get(i);
            }
        }
        list.add(0, spot_name+"_m_0_0");
        return list;
    }

}
