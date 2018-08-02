package com.silentnotary.widget.textview;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

import com.silentnotary.util.CacheUtil;


/**
 * Created by albert on 9/2/17.
 */

public class FontCache {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface get(String name, Context context) {
        name = "fonts/" + name + ".ttf";
        Typeface tf = fontCache.get("fonts/" + name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);

            } catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }

    public static Typeface getFromFile(String name, Context context) {
        Typeface tf;
        try {
             tf = Typeface.createFromFile(new CacheUtil().readFileFromCache(context, name));
        } catch (Exception e) {
            return null;
        }
        fontCache.put(name, tf);
        return tf;
    }
}
