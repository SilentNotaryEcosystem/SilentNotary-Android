package com.silentnotary.widget.textview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by albert on 10/13/17.
 */

public class MyFilesTextView  extends TextViewBase {

    public MyFilesTextView(Context context) {
        super(context);
    }

    public MyFilesTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFilesTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public TextInfo getTextAttribute() {
        return new TextInfo(16, TextViewBase.OPENSANS_REGULAR);
    }

}
