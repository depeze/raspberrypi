package hu.depeze.dhtviewer;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements HTTPGetAsyncResponse {

    private Resources resources = null;

    private TextView temperatureTextView = null;
    private TextView temperatureTimestampTextView = null;
    private TextView humidityTextView = null;
    private TextView humidityTimestampTextView = null;

    private double lastTemperature = 25;
    private String lastTemperatureTimestamp = "";
    private double lastHumidity = 50;
    private String lastHumidityTimestamp = "";

    private TextView connectionStatusTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resources = super.getResources();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        super.setSupportActionBar(toolbar);

        temperatureTextView = (TextView) super.findViewById(R.id.textView2);
        temperatureTextView.setText(resources.getString(R.string.temperature, lastTemperature));
        temperatureTimestampTextView = (TextView) super.findViewById(R.id.textView5);
        temperatureTimestampTextView.setText(lastTemperatureTimestamp);

        humidityTextView = (TextView) super.findViewById(R.id.textView4);
        humidityTextView.setText(resources.getString(R.string.humidity, lastHumidity));
        humidityTimestampTextView = (TextView) super.findViewById(R.id.textView6);
        humidityTimestampTextView.setText(lastHumidityTimestamp);

        connectionStatusTextView = (TextView) super.findViewById(R.id.textView8);
        connectionStatusTextView.setText("");

        DHTUtils.refresh(super.getApplicationContext(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                super.startActivity(settingsIntent);
                break;
            case R.id.action_refresh:
                DHTUtils.refresh(super.getApplicationContext(), this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processHTTPGetResult(String httpResponse) {
        Log.v(Constants.TAG, "Main Activity received HTTP Response: " + httpResponse);

        try {
            JSONObject json = new JSONObject(httpResponse);

            JSONObject temperatureJSON = json.getJSONObject("temperature");
            long temperatureTimestamp = temperatureJSON.getLong("timestamp");
            lastTemperatureTimestamp = longTimestampToString(temperatureTimestamp);
            temperatureTimestampTextView.setText(lastTemperatureTimestamp);
            double temperatureValue = temperatureJSON.getDouble("value");
            lastTemperature = temperatureValue;
            Log.v(Constants.TAG, "Temperature: " + temperatureTimestamp + " " + temperatureValue);
            temperatureTextView.setText(resources.getString(R.string.temperature, temperatureValue));

            JSONObject humidityJSON = json.getJSONObject("humidity");
            long humidityTimestamp = humidityJSON.getLong("timestamp");
            lastHumidityTimestamp = longTimestampToString(humidityTimestamp);
            humidityTimestampTextView.setText(lastHumidityTimestamp);
            double humidityValue = humidityJSON.getDouble("value");
            lastHumidity = humidityValue;
            Log.v(Constants.TAG, "Humidity: " + humidityTimestamp + " " + humidityValue);
            humidityTextView.setText(resources.getString(R.string.humidity, humidityValue));

            handleConnectionOK();
        } catch (JSONException ex) {
            Log.w(Constants.TAG, ex.getMessage());
            handleConnectionError();
        }
    }

    private void handleConnectionError() {
        connectionStatusTextView.setText(resources.getString(R.string.connection_status, "ERROR"));
        connectionStatusTextView.setTextColor(resources.getColor(R.color.colorConnectionStatusERROR));
    }

    private void handleConnectionOK() {
        connectionStatusTextView.setText(resources.getString(R.string.connection_status, "OK"));
        connectionStatusTextView.setTextColor(resources.getColor(R.color.colorConnectionStatusOK));
    }

    private static String longTimestampToString(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss z", Locale.ENGLISH);
        return simpleDateFormat.format(new Date(timestamp));
    }

}
