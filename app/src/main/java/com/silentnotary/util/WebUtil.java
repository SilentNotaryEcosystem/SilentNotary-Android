package com.silentnotary.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by albert on 10/4/17.
 */

public class WebUtil {

    public static void openWebpage(String url, Activity activity) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(browserIntent);
        } catch (Exception e) {
        }
    }
}
