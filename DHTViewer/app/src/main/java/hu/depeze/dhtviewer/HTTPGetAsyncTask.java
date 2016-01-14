package hu.depeze.dhtviewer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by depeze on 2016.01.10..
 */
public class HTTPGetAsyncTask extends AsyncTask<URL, Integer, String> {

    private HTTPGetAsyncResponse delegateResponse = null;

    public HTTPGetAsyncTask(HTTPGetAsyncResponse response) {
        this.delegateResponse = response;
    }

    @Override
    protected String doInBackground(URL... params) {
        URL url = params[0];
        Log.v(Constants.TAG, "URL: " + url.toString());

        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            Log.v(Constants.TAG, "HTTP " + responseCode);

            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
                String response = IOUtils.toString(inputStream);
                Log.v(Constants.TAG, "HTTP Response: " + response);
                return response;
            }
        } catch (IOException ex) {
            Log.e(Constants.TAG, ex.getMessage(), ex);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    //
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        delegateResponse.processHTTPGetResult(result);
    }
}
