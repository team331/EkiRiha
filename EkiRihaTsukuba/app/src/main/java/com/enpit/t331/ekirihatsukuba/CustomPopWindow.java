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
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * Created by woo on 16/10/24.
 */
public class CustomPopWindow extends PopupWindow{
    private View mView;
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
        this.setOutsideTouchable(true);
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

    private void initView(final Activity context, View.OnClickListener itemOnClick, String name){
        switch(name){
            case "introduce":
                initIntroduce(context, itemOnClick);break;
            case "setting":
                initSetting(context, itemOnClick);break;
        }
    }

    private void initIntroduce(final Activity context, View.OnClickListener itemOnClick){
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.popupwindow_introduce, null);
        Button button = (Button) mView.findViewById(R.id.button);
        button.setOnClickListener(itemOnClick);


    }

    private void initSetting(final Activity context, View.OnClickListener itemOnClick){
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.popupwindow_setting, null);
        Button button = (Button) mView.findViewById(R.id.button);
        button.setOnClickListener(itemOnClick);
    }

    public void backgroundAlpha(Activity context, float bgAlpha){
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
