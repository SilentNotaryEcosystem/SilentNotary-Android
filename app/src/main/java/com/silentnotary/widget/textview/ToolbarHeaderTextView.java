package com.silentnotary.widget.textview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by albert on 10/13/17.
 */

public class ToolbarHeaderTextView extends TextViewBase {

    public ToolbarHeaderTextView(Context context) {
        super(context);
    }

    public ToolbarHeaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolbarHeaderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public TextInfo getTextAttribute() {
        return new TextInfo(18, TextViewBase.OPENSANS_REGULAR);
    }
}
