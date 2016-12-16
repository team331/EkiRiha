package com.enpit.t331.ekirihatsukuba;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by woo on 16/10/24.
 */
public class CustomPopWindow extends PopupWindow{
    private View mView;
    private DataManager dataManager;
    private InputMethodManager imm;
    private EditText editText = null;
    public CustomPopWindow(Activity context, String name){
        super(context);
        initView(context, null, name);
    }
    public CustomPopWindow(final Activity context, View.OnClickListener itemOnClick, String name){
        super(context);
        initView(context, itemOnClick, name);
        this.setContentView(mView);
        this.setWidth(900);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setTouchable(true);
//        this.setAnimationStyle(R.style.select_);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(false);
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        backgroundAlpha(context, 0.5f);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(context, 1f);
            }
        });
    }

    public CustomPopWindow(final Activity context, View.OnClickListener itemOnClick, String name, DataManager dm){
        super(context);
        this.dataManager = dm;
        initView(context, itemOnClick, name);
        this.setContentView(mView);
        this.setWidth(900);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setTouchable(true);
//        this.setAnimationStyle(R.style.select_);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(false);
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        backgroundAlpha(context, 0.5f);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(context, 1f);
            }
        });



    }

    private void initView(final Activity context, View.OnClickListener itemOnClick, String name){
        switch(name){
            case "introduce":
                initIntroduce(context, itemOnClick);break;
            case "setting":
                initSetting(context, itemOnClick);break;
            case "memo":
                initMemo(context, itemOnClick);break;
        }
    }

    private void initIntroduce(final Activity context, View.OnClickListener itemOnClick){
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.popupwindow_introduce, null);
        Button button = (Button) mView.findViewById(R.id.button);
        button.setOnClickListener(itemOnClick);

        CheckBox checkBox = (CheckBox) mView.findViewById(R.id.checkBox);



    }

    public void checkBoxOperate(DataManager dm, String name){
        CheckBox checkBox = (CheckBox) mView.findViewById(R.id.checkBox);
        if(checkBox != null){
            if(checkBox.isChecked()){
                dm.setBoolean(name, false);
            }
        }
    }//show_introduce, show_setting


    private void initSetting(final Activity context, View.OnClickListener itemOnClick){
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.popupwindow_setting, null);
        Button button = (Button) mView.findViewById(R.id.button);
        button.setOnClickListener(itemOnClick);
    }

    private void initMemo(final Activity context, View.OnClickListener itemOnClick){
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.popupwindow_memo, null);
        editText = (EditText) mView.findViewById(R.id.editText);
        editText.setText(loadMemoData());
        editText.setFocusable(true);
        ImageButton button = (ImageButton) mView.findViewById(R.id.imageButton);
        button.setOnClickListener(itemOnClick);
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                editText.requestFocus();
                imm = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 400);

    }

    public void saveMemoData(){
        imm.hideSoftInputFromInputMethod(editText.getWindowToken(), 0);
        if(editText!=null)
            dataManager.setString("memo", editText.getText().toString());

    }

    private String loadMemoData(){
        return dataManager.getString("memo");
    }
    public void backgroundAlpha(Activity context, float bgAlpha){
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
