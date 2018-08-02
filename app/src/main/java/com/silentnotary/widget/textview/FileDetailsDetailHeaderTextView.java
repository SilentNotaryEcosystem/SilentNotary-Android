package com.silentnotary.widget.textview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by albert on 10/12/17.
 */

public class FileDetailsDetailHeaderTextView extends TextViewBase {

    public FileDetailsDetailHeaderTextView(Context context) {
        super(context);
    }

    public FileDetailsDetailHeaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FileDetailsDetailHeaderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public TextInfo getTextAttribute() {
        return new TextInfo(15, TextViewBase.OPENSANS_LIGHT);
    }
}
