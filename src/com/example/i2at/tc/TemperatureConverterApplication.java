/**
 * 
 */

package com.example.i2at.tc;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * @author brldmila
 */
public class TemperatureConverterApplication extends Application {
    private static final String TAG =
            "TemperatureConverterApplication";

    public static final int DECIMAL_PLACES_DEFAULT = 2;
    public static final String DECIMAL_PLACES = "decimalPlaces";

    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void setDecimalPlaces(int d) {
        final Editor editor = mSharedPreferences.edit();
        editor.putString(DECIMAL_PLACES, Integer.toString(d));
        editor.commit();
    }

    public int getDecimalPlaces() {
        return Integer.parseInt(
                mSharedPreferences.getString(DECIMAL_PLACES,
                        Integer.toString(DECIMAL_PLACES_DEFAULT)));
    }

}
