package com.example.hermon.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hermon.weather.data.WeatherResult;
import com.example.hermon.weather.network.WeatherAPI;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowCityWeather extends AppCompatActivity {

    private final String APIKEY = "7f8172f07ea5f6a6a1cc2f84b6789579";
    private final String BaseAPIURL = "http://api.openweathermap.org";
    private final String IMG_BaseURL = "http://openweathermap.org/img/w/";
    private final String DEGREE = "°";
    private TextView city_tv,temp_tv,tempMin_tv,tempMax_tv,humid_tv,descrip_tv,error_tv;
    private ImageView icon_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_city_weather);

        initViews();

        if (getIntent().hasExtra(MainActivity.CITY_NAME)){
            String cityName = getIntent().getStringExtra(MainActivity.CITY_NAME);
            getWeatherInfo(cityName);
        }
    }

    private void initViews() {
        city_tv = (TextView) findViewById(R.id.details_city);
        temp_tv = (TextView) findViewById(R.id.details_temp);
        tempMax_tv = (TextView) findViewById(R.id.details_tempMax);
        tempMin_tv = (TextView) findViewById(R.id.details_tempMin);
        humid_tv = (TextView) findViewById(R.id.details_humidity);
        descrip_tv = (TextView) findViewById(R.id.details_description);
        error_tv = (TextView) findViewById(R.id.details_error);
        icon_iv = (ImageView) findViewById(R.id.details_icon);
    }

    private void getWeatherInfo(String cityName) {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BaseAPIURL).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        Call<WeatherResult> callWeatherInfo = weatherAPI.getWeather(cityName,
                "metric",APIKEY);
        error_tv.setText("Please Wait...");
        callWeatherInfo.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                setResultView(response.body());
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                error_tv.setText(t.getLocalizedMessage());
            }
        });
    }

    private void setResultView(WeatherResult body) {
        try {
            city_tv.setText(body.getName());

            temp_tv.setText(body.getMain().getTemp() + DEGREE + "c");
            tempMax_tv.setText(body.getMain().getTempMax() + DEGREE + "c");
            tempMin_tv.setText(body.getMain().getTempMin() + DEGREE + "c");

            Glide.with(this).load(IMG_BaseURL + body.getWeather().get(0).getIcon() + ".png").
                    into(icon_iv);
            descrip_tv.setText(body.getWeather().get(0).getMain());

            humid_tv.setText("Humidity: " + body.getMain().getHumidity() + "%");
            error_tv.setText("");
        }
        catch (NullPointerException n){
            error_tv.setText("Couldn't find city. Please be more specific");
        }
    }
}
