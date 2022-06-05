package com.example.weatherRemake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String API = "aa29d1f1d28b3fd85b44d594b8519462";
    String SEARCHCITY;
    EditText enterCity;
    ImageView search;
    TextView city,country,time,temp,forecast,humidity,min_temp,max_temp,sunrises,sunsets,pressure,windSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        {

            enterCity = (EditText) findViewById(R.id.Your_city);
            search = (ImageView) findViewById(R.id.search);
            city = (TextView) findViewById(R.id.city);
            country = (TextView) findViewById(R.id.country);
            time = (TextView) findViewById(R.id.time);
            temp = (TextView) findViewById(R.id.temp);
            forecast = (TextView) findViewById(R.id.forecast);
            humidity = (TextView) findViewById(R.id.humidity);
            min_temp = (TextView) findViewById(R.id.min_temp);
            max_temp = (TextView) findViewById(R.id.max_temp);
            sunrises = (TextView) findViewById(R.id.sunrises);
            sunsets = (TextView) findViewById(R.id.sunsets);
            pressure = (TextView) findViewById(R.id.pressure);
            windSpeed = (TextView) findViewById(R.id.wind_speed);

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SEARCHCITY = enterCity.getText().toString();
                    new weatherTask().execute();
                }
            });
        }
    }

     class weatherTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + SEARCHCITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {

            try {
                JSONObject JSON = new JSONObject(response);
                JSONObject main = JSON.getJSONObject("main");
                JSONObject weather = JSON.getJSONArray("weather").getJSONObject(0);
                JSONObject wind = JSON.getJSONObject("wind");
                JSONObject sys = JSON.getJSONObject("sys");


                String city_name = JSON.getString("name");
                String countryname = sys.getString("country");
                Long updatedAt = JSON.getLong("dt");
                String updatedAtText = "Last Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temperature = main.getString("temp");
                String cast = weather.getString("description");
                String humidityTxt = main.getString("humidity");
                String temp_min = main.getString("temp_min");
                String temp_max = main.getString("temp_max");
                String pre = main.getString("pressure");
                String windspeed = wind.getString("speed");
                Long rise = sys.getLong("sunrise");
                String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
                Long set = sys.getLong("sunset");
                String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));

                city.setText(city_name);
                country.setText(countryname);
                time.setText(updatedAtText);
                temp.setText(temperature + "°C");
                forecast.setText(cast);
                humidity.setText(humidityTxt);
                min_temp.setText(temp_min);
                max_temp.setText(temp_max);
                sunrises.setText(sunrise);
                sunsets.setText(sunset);
                pressure.setText(pre);
                windSpeed.setText(windspeed);

            } catch (Exception e) {

                Toast.makeText(MainActivity.this, "Error:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
