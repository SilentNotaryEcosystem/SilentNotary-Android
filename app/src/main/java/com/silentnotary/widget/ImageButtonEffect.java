package com.silentnotary.widget;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by albert on 10/3/17.
 */

public class ImageButtonEffect implements View.OnTouchListener {

    PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
    private View.OnClickListener onClickListener;
    private int filtercolor = Color.parseColor("#E8E8E8");
    public static int LIGHT_GRAY_COLOR = Color.parseColor("#E8E8E8");

    public ImageButtonEffect(View.OnClickListener onClickListener, int filtercolor) {
        this.onClickListener = onClickListener;
        this.filtercolor = filtercolor;
    }

    public ImageButtonEffect(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ImageButtonEffect(View.OnClickListener onClickListener, PorterDuff.Mode mode) {
        this.onClickListener = onClickListener;
        this.mode = mode;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                try {
                    if (v instanceof ImageButton)
                        v.getBackground().setColorFilter(filtercolor, mode);
                    else if (v instanceof ImageView)
                        ((ImageView) v).getDrawable().setColorFilter(filtercolor, mode);
                    else if (v instanceof RelativeLayout
                            || v instanceof FrameLayout
                            || v instanceof LinearLayout) {
                        if (v.getBackground() != null)
                            v.getBackground().setColorFilter(filtercolor, mode);
                    }
                } catch (Exception e) {
                }
                v.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            case MotionEvent.ACTION_CANCEL: {
                try {
                    if (v instanceof ImageButton)
                        v.getBackground().clearColorFilter();
                    else if (v instanceof ImageView)
                        ((ImageView) v).getDrawable().clearColorFilter();
                    else if (v instanceof RelativeLayout
                            || v instanceof FrameLayout
                            || v instanceof LinearLayout) {
                        if (v.getBackground() != null)
                            v.getBackground().clearColorFilter();
                    }
                } catch (Exception e) {

                }
                v.invalidate();
                break;
            }
        }
        return true;

    }
}
