package com.example.regent.moviegalleriapart_2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;

public class Utils {

    public static boolean isSameDomain(String url, String url1){
        return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
    }

    private static String getRootDomainUrl(String url){
        String[] domainKeys = url.split("/")[2].split("\\.");
        int length = domainKeys.length;
        int dummy = domainKeys[0].equals("www") ? 1 : 0;
        if (length - dummy == 2){
            return domainKeys[length - 2] + "."  + domainKeys[length - 1];
        }
        else {
            if (domainKeys[length - 1].length() == 2){
                return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
            } else {
                return domainKeys[length - 2] + "." + domainKeys[length - 1];
            }
        }
    }

    public static void tintMenuIcon(Context context, MenuItem menuItem, int color){
        Drawable drawable = menuItem.getIcon();
        if (drawable != null){
            // If we don't mutate the drawable, then all drawables with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static void bookmarkUrl(Context context, String url){
        SharedPreferences preferences = context.getSharedPreferences("movie", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();

        // if url is already bookmarked, unbookmark it
        if (preferences.getBoolean(url, false)){
            editor.putBoolean(url, false);
        } else {
            editor.putBoolean(url, true);
        }
        editor.commit();
    }

    public static boolean isBookmarked(Context context, String url){
        SharedPreferences preferences = context.getSharedPreferences("movie", 0);
        return preferences.getBoolean(url, false);
    }

}
