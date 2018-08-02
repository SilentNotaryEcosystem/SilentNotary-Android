package com.silentnotary.widget.textview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by albert on 10/12/17.
 */


public abstract class TextViewBase extends android.support.v7.widget.AppCompatTextView {

    public static final String OPENSANS_REGULAR = "OpenSans-Regular";
    public static final String OPENSANS_LIGHT = "OpenSans-Light";
    public static final String OPENSANS_BOLD = "OpenSans-Bold";

    public TextViewBase(Context context) {
        super(context);
        init(getTextAttribute());
    }

    public TextViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(getTextAttribute());
    }


    public TextViewBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(getTextAttribute());
    }

    public abstract TextInfo getTextAttribute();

    public void init(TextInfo textInfo) {
        if (isInEditMode()) return;

        this.setTypeface(FontCache.get(textInfo.font, getContext()));
        this.setTextSize(textInfo.size);
    }

    public static class TextInfo {

        public int size;
        public String font;

        public TextInfo(int size, String font) {
            this.size = size;
            this.font = font;
        }
    }
}
