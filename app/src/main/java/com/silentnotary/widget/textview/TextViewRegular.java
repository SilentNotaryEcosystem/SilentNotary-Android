package com.silentnotary.widget.textview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by albert on 10/14/17.
 */

public class TextViewRegular extends TextViewBase {

    public TextViewRegular(Context context) {
        super(context);
    }

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public TextInfo getTextAttribute() {
        return new TextInfo(16, TextViewBase.OPENSANS_REGULAR);
    }
}