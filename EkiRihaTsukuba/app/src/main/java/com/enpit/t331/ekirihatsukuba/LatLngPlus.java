package com.enpit.t331.ekirihatsukuba;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by woo on 16/10/25.
 */
public class LatLngPlus {
    public LatLng latlng;
    public String tag;
    private int local_number;
    private ArrayList<Integer> neighbor = new ArrayList<>();
    private boolean station = false;
    private boolean build = false;

    public LatLngPlus(double x, double y, int number){
        latlng = new LatLng(x,y);
        this.local_number = number;
    }

    public void setIsStation(){
        station = true;
    }

    public void setIsBuild(){
        build = true;
    }

    public boolean isStation(){
        return station;
    }

    public boolean isBuild(){
        return build;
    }

    public void addNeighbor(int num){
        neighbor.add(num);
    }

    public ArrayList<Integer> getNeighbor(){return neighbor;}

    public ArrayList<Integer> getUnVisitedNeighbor(ArrayList<Integer> visited){
        ArrayList<Integer> result = new ArrayList<>(neighbor);
        for(Integer item: visited){
            if(result.contains(item)){
                result.remove(item);
            }
        }

        return result;
    }
    public boolean isBranch(){
        if(neighbor.size() >3)
            return true;
        else
            return false;
    }
}
