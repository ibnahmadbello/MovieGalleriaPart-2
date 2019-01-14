package com.example.regent.moviegalleriapart_2.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class FavouritePreference {

    private static final String PREF_FAVOURITE_QUERY = "favouriteQuery";

    public static int getPrefFavouriteQuery(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_FAVOURITE_QUERY, -1);
    }

    public static void setPrefFavouriteQuery(Context context, int query){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_FAVOURITE_QUERY, query)
                .apply();
    }

}
