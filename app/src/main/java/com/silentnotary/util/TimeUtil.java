package com.silentnotary.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by albert on 9/27/17.
 */

public class TimeUtil {

    public static String formatTimestampWithSeconds(long timestamp){
        Date date = new Date(timestamp);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        return df.format("yyyy-MM-dd hh:mm:ss", date).toString();
    }

    public static String formatTimestamp(long timestamp){
        Date date = new Date(timestamp);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        return df.format("yyyy-MM-dd hh:mm", date).toString();
    }

    public static String timestampToJsonDate(long timestamp){
        return String.format("/Date(%d)/", timestamp);
    }

    public static long parseJsonDate(String date){
        long timestamp = 0;
        try {
            Pattern pattern = Pattern.compile("(Date\\()(.*?)(\\+.+?\\))");
            Matcher matcher = pattern.matcher(date);
            if(matcher.find())
                return Long.parseLong( matcher.group(2));
        }catch (Exception e){

        }
        return timestamp;
    }
}
