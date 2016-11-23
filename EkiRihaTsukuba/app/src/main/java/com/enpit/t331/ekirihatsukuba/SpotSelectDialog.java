package com.enpit.t331.ekirihatsukuba;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by 12t4015h on 2016/11/15.
 */

public class SpotSelectDialog extends DialogFragment {
    private String scholarship_str = "";
    private static Spinner mSchoolGroupSpinner;
    private static Spinner mScholarshipSpinner;
    private static String[] mArray;
    private int mSpotId = 0;//試験会場
    private int mSchoolGroup = 0;//選択された学群
    private int mScholarship = 0;//選択された学類


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.spot_select_dialog,(ViewGroup)getActivity().findViewById(R.id.spot_select_dialog_layout));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("学群と学部を選択してください");
        builder.setView(layout);
        builder.setPositiveButton("OK",null);

        AdapterView.OnItemSelectedListener mSchoolGroupListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // 学群スピナーの選択したアイテムの場所を取得
                mSchoolGroup = parent.getSelectedItemPosition();

                scholarship_str = "scholarship_" + String.valueOf(mSchoolGroup);

                // 取得した文字列から配列リソースIDを取得
                int rID = getResources().getIdentifier(scholarship_str, "array", getActivity().getPackageName());

                // 取得した配列リソースIDを文字列配列に格納
                mArray = getResources().getStringArray(rID);

                // 学類スピナー取得
                mScholarshipSpinner = (Spinner)layout.findViewById(R.id.scholarship_spinner);

                // 配列リソースIDから取得した文字列配列をアダプタに入れる
                ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item);
                for (int i = 0; i < mArray.length; i++) {
                    mAdapter.add(mArray[i]);
                }

                mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // アダプタを学類スピナーにセット
                mScholarshipSpinner.setAdapter(mAdapter);
                mScholarship = 0;
                //試験会場更新
                checkSpots();
                TextView textView = (TextView)layout.findViewById(R.id.textView13);
                textView.setText(getResources().getStringArray(R.array.spot)[mSpotId]);
            }

            @Override
            // / 何も選択されてない場合
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        AdapterView.OnItemSelectedListener mScholarshipListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 学類スピナーの選択したアイテムの場所を取得
                mScholarship = parent.getSelectedItemPosition();
                //試験会場更新
                checkSpots();
                TextView textView = (TextView)layout.findViewById(R.id.textView13);
                textView.setText(getResources().getStringArray(R.array.spot)[mSpotId]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        // 学群スピナー取得
        String[] schoolGroupData = getResources().getStringArray(R.array.school_group);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,schoolGroupData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSchoolGroupSpinner = (Spinner)layout.findViewById(R.id.school_group_spinner);
        mSchoolGroupSpinner.setAdapter(spinnerAdapter);
        mSchoolGroupSpinner.setOnItemSelectedListener(mSchoolGroupListener);

        // 学類スピナー取得
        String[] scholarshipData = getResources().getStringArray(R.array.scholarship_0);
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,scholarshipData);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mScholarshipSpinner = (Spinner)layout.findViewById(R.id.scholarship_spinner);
        mScholarshipSpinner.setAdapter(spinnerAdapter2);
        mScholarshipSpinner.setOnItemSelectedListener(mScholarshipListener);

        return builder.create();
    }

    private void checkSpots(){
        switch(mSchoolGroup){
            case 0://人文・文化学群
                if(mScholarship == 0){mSpotId = 0;}else{mSpotId = 1;}
                break;
            case 1://社会・国際学群
                if(mScholarship == 0){mSpotId = 0;}else{mSpotId = 2;}
                break;
            case 2://人間学群
                mSpotId = 1;
                break;
            case 3://生命環境学群
                if(mScholarship == 2){mSpotId = 0;}else{mSpotId = 1;}
                break;
            case 4://理工学群
                if(mScholarship == 0 || mScholarship == 1 || mScholarship == 2){
                    mSpotId = 0;
                }else{
                    mSpotId = 2;
                }
                break;
            case 5://情報学群
                if(mScholarship == 0){mSpotId = 2;}else{mSpotId = 3;}
                break;
            case 6://医学群
                mSpotId = 4;
                break;
            case 7://体育専門学群
            case 8://芸術専門学群
                mSpotId = 5;
                break;
        }
    }

    //学群学類に対応する試験会場を返す(第一試験会場=0....)
    public int getSpotId(){
        return mSpotId;
    }
}
