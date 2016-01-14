package hu.depeze.dhtviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by depeze on 2016.01.11..
 */
public class DHTUtils {

    public static String getDHTURL(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(Constants.PREF_URL, "N/A");
    }

    public static void refresh(Context context, HTTPGetAsyncResponse httpGetAsyncResponse) {
        try {
            URL url = new URL(getDHTURL(context));
            new HTTPGetAsyncTask(httpGetAsyncResponse).execute(url);
        } catch (MalformedURLException ex) {
            Log.e(Constants.TAG, ex.getMessage(), ex);
        }
    }

}
