package com.silentnotary.widget.textview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by albert on 10/16/17.
 */

public class TextViewRegularSmall extends TextViewBase {
    public TextViewRegularSmall(Context context) {
        super(context);
    }

    public TextViewRegularSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewRegularSmall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public TextInfo getTextAttribute() {
        return new TextInfo(14, TextViewBase.OPENSANS_REGULAR);
    }

}
