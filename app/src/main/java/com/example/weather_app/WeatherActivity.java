package com.example.weather_app;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    TextView temperatureText, cityText, conditionText, windText,precipText,humidityText;
    LinearLayout forecastContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        temperatureText = findViewById(R.id.temperatureText);
        cityText = findViewById(R.id.cityText);
        conditionText = findViewById(R.id.conditionText);
        windText=findViewById(R.id.windText);
        precipText=findViewById(R.id.precip);
        humidityText=findViewById(R.id.humid);
        forecastContainer = findViewById(R.id.forecastContainer);



        fetchWeather("Tunis"); // Example city
    }

    private void fetchWeather(String cityName) {
        String apiKey = "760b9ea245044dfeb5c101723251304";
        String url = "https://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + cityName + "&days=7";


        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject location = response.getJSONObject("location");
                        JSONObject current = response.getJSONObject("current");
                        JSONObject condition = current.getJSONObject("condition");
                        JSONArray forecastArray = response.getJSONObject("forecast").getJSONArray("forecastday");
                        forecastContainer.removeAllViews();

                        for (int i = 0; i < forecastArray.length(); i++) {
                            JSONObject dayObj = forecastArray.getJSONObject(i);
                            String date = dayObj.getString("date");

                            JSONObject day = dayObj.getJSONObject("day");
                            double maxTemp = day.getDouble("maxtemp_c");
                            String cond = day.getJSONObject("condition").getString("text");

                            // Inflate the forecast_item view
                            View forecastView = getLayoutInflater().inflate(R.layout.forecast_item, null);

                            // Set values
                            TextView dateText = forecastView.findViewById(R.id.dateText);
                            TextView tempText = forecastView.findViewById(R.id.tempText);
                            TextView conditionText = forecastView.findViewById(R.id.conditionText);

                            dateText.setText(date);
                            tempText.setText(String.format("%.1f°C", maxTemp));
                            conditionText.setText(cond);

                            // Add view to container
                            forecastContainer.addView(forecastView);
                        }

                        String city = location.getString("name");
                        double temp = current.getDouble("temp_c");
                        String description = condition.getString("text");
                        double wind = current.getDouble("wind_mph");
                        double precip=current.getDouble("precip_mm");
                        double humidity=current.getDouble("humidity");

                        // Set values to TextViews
                        temperatureText.setText(temp + "°C");
                        cityText.setText(city);
                        conditionText.setText(description);
                        windText.setText(String.valueOf(wind));
                        precipText.setText(String.valueOf(precip));
                        humidityText.setText(String.valueOf(humidity));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        queue.add(jsonObjectRequest);
    }

}
