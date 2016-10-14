package edu.calvin.cs262.lab06;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
*               QUESTIONS
* What does the application do for invalid citys?
*   A toast appears that says can not connect to server. However some invalid citys will still produce weather data.
*
*   What is the API key? What does it Do?
*   8301442678d36413b4e3a04d016deee7 is the API key. The key identifies the program and user to the website
*
*   what does teh full JSON response look like?
*   The full json response is huge. It holds all of the weather information identifiable by keywords so that the app can parse the info.
*
*   What does tthe system do with the JSON data?
*   The app looks for the keyword of dt. there will be seven of them, one for each day of the week.
*   then it looks for the needed keywords, like min, max, description, and pulls all of the values.
*
*   what is the Weather class designed to do?
*   The class models a weather object, it converts the date into a readable format and provides an accessor for the
*   rest of the instance variables.
*/






/**
 * Reads openweathermap's RESTful API for weather forecasts.
 * The code is based on Deitel's WeatherViewer (Chapter 17), simplified based on Murach's NewsReader (Chapter 10).
 * <p>
 * for CS 262, lab 6
 *
 * @author kvlinden
 * @version summer, 2016
 */
public class MainActivity extends AppCompatActivity {

    private EditText cityText;
    private Button fetchButton;

    private List<Weather> weatherList = new ArrayList<>();
    private ListView itemsListView;

    /* This formater can be used as follows to format temperatures for display.
     *     numberFormat.format(SOME_DOUBLE_VALUE)
     */
    //private NumberFormat numberFormat = NumberFormat.getInstance();

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityText = (EditText) findViewById(R.id.cityText);
        fetchButton = (Button) findViewById(R.id.fetchButton);
        itemsListView = (ListView) findViewById(R.id.weatherListView);

        // See comments on this formatter above.
        //numberFormat.setMaximumFractionDigits(0);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard(cityText);
                new GetWeatherTask().execute(createURL(cityText.getText().toString()));
            }
        });
    }

    /**
     * Formats a URL for the webservice specified in the string resources.
     *
     * @param city the target city
     * @return URL formatted for openweathermap.com
     */
    private URL createURL(String city) {
        try {
            String urlString = getString(R.string.web_service_url) +
                    URLEncoder.encode(city, "UTF-8") +
                    "&units=" + getString(R.string.openweather_units) +
                    "&cnt=" + getString(R.string.openweather_count) +
                    "&APPID=" + getString(R.string.openweather_api_key);
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    /**
     * Deitel's method for programmatically dismissing the keyboard.
     *
     * @param view the TextView currently being edited
     */
    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Inner class for GETing the current weather data from openweathermap.org asynchronously
     */
    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return new JSONObject(result.toString());
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weather) {
            if (weather != null) {
                //Log.d(TAG, weather.toString());
                convertJSONtoArrayList(weather);
                MainActivity.this.updateDisplay();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Converts the JSON weather forecast data to an arraylist suitable for a listview adapter
     *
     * @param forecast
     */
    private void convertJSONtoArrayList(JSONObject forecast) {
        weatherList.clear(); // clear old weather data
        try {
            JSONArray list = forecast.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject day = list.getJSONObject(i);
                JSONObject temperatures = day.getJSONObject("temp");
                String low = Integer.toString(temperatures.getInt("min"));
                String high = Integer.toString(temperatures.getInt("max"));
                JSONObject weather = day.getJSONArray("weather").getJSONObject(0);
                weatherList.add(new Weather(
                        day.getLong("dt"),
                        weather.getString("description"), low, high));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh the weather data on the forecast ListView through a simple adapter
     */
    private void updateDisplay() {
        if (weatherList == null) {
            Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (Weather item : weatherList) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("day", item.getDay());
            map.put("description", item.getSummary());
            map.put("low", item.getLow());
            map.put("high", item.getHigh());
            data.add(map);
        }

        int resource = R.layout.weather_item;
        String[] from = {"day", "description", "low", "high"};
        int[] to = {R.id.dayTextView, R.id.summaryTextView, R.id.LowTempView ,R.id.HighTempView};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }

}
