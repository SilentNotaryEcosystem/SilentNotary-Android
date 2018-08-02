package com.silentnotary.widget.textview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by albert on 10/15/17.
 */

public class LargeButtonTextView extends TextViewBase {

    public LargeButtonTextView(Context context) {
        super(context);
    }

    public LargeButtonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LargeButtonTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public TextInfo getTextAttribute() {
        return new TextInfo(15, TextViewBase.OPENSANS_LIGHT);
    }
}
