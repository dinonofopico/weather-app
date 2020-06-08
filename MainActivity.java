package za.co.dvtweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Weather> weathers;
    List arr;
    Adapter adapter;
    Weather weather;
    TextView currentTemp, minTemp, maxTemp, currentTemp1;
    ImageView currentTempImg;
    FusedLocationProviderClient fusedLocationProviderClient;
    ConstraintLayout main_container;
    private String API_KEY = "&appid="+ "902d455b6cd341e39b4d3c2916e21a0e";
    private String WEATHER_API = "https://api.openweathermap.org/data/2.5/forecast?" +
            "units=metric";
    int dayOfTheWeek;
    Date date;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String [] dayOfTheWeekName = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday", "Sunday"};
    int [] weatherIcon ={R.drawable.clear, R.drawable.clear2x, R.drawable.clear3x,
            R.drawable.partlysunny, R.drawable.partlysunny2x, R.drawable.partlysunny3x,
    R.drawable.rain, R.drawable.rain2x, R.drawable.rain3x},
            mainIcon = {R.drawable.forest_sunny,
            R.drawable.forest_cloudy, R.drawable.forest_rainy};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.weather_lists);

        calendar = Calendar.getInstance();
        date = new Date();
        calendar.setTime(date);
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOfTheWeek = dayOfTheWeek - 1;

        weathers = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        checkLocationPermission();

    }

// the function for getting permission on user's device
    private void checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

    // the function to get the current user's location

    private void getLocation() {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().
                addOnCompleteListener(new OnCompleteListener<Location>() {

            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                Location location1;


                if(location != null){

                    Geocoder geocoder = new Geocoder(MainActivity.this,
                            Locale.getDefault());


                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);

                        double dlat = addresses.get(0).getLatitude(),
                                dlon = addresses.get(0).getLongitude();

                        String lat = Double.toString(dlat), lon = Double.toString(dlon), latlon;

                        latlon = "lat="+lat+"&"+"lon="+lon;

                        String url = WEATHER_API +"&"+latlon + "&"+ API_KEY;

                        weatherData(url);

//                        Toast.makeText(getApplicationContext(),
//                                Integer.toString(dayOfTheWeek)
//                                , Toast.LENGTH_LONG).show();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                }

            }
        });

    }

    // function for getting data from the api

    private void weatherData(String url) {

        StringRequest request = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("res: ", response.toString());


                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray weatherData = jsonObject.getJSONArray("list");

                            int tempDay, nextDay = 0, sky_cover = 0;



                            for(int i = 0; i < 6; i++ ){

                                String temp, temp_min, temp_max;
                                int t, tmin, tmax;

                                weather = new Weather();

                                JSONObject tempDetails = null, sky = null;
                                JSONArray  weatherDetails = null;

                                JSONObject tempObj = weatherData.getJSONObject(i);

                                tempDetails = tempObj.getJSONObject("main");

                                weatherDetails = tempObj.getJSONArray("weather");

                                sky = weatherDetails.getJSONObject(0);

                                String condition = sky.getString("main");



                                temp = tempDetails.getString("temp");
                                temp_min = tempDetails.getString("temp_min");
                                temp_max = tempDetails.getString("temp_max");

                                t = Math.round(Float.parseFloat(temp));
                                tmin = Math.round(Float.parseFloat(temp_min));
                                tmax = Math.round(Float.parseFloat(temp_max));

                                temp = Float.toString(t);
                                temp_min = Float.toString(tmin);
                                temp_max = Float.toString(tmax);



                                // set a day of the week

                                    if((dayOfTheWeek >= 1 && dayOfTheWeek < 7) && i > 0){

                                        nextDay = dayOfTheWeek + 1;

                                        dayOfTheWeek = nextDay;
                                    }else if(dayOfTheWeek == 7){

                                        nextDay = 1;
                                        dayOfTheWeek = nextDay;

                                    }

                                ///////////////////////


                                // update the main picture

                                if(condition.toLowerCase().contains("rain")){

                                    sky_cover = 2;

                                }else if (condition.toLowerCase().contains("cloudy")){

                                    sky_cover = 1;
                                }else{

                                    sky_cover = 0;
                                }


                                if(i == 0){

                                    weather.setBgColor(getResources().getColor(R.color.forestSunny,
                                            null));
                                    weather.setPosition(i);
                                    weather.setTemperature(temp+"\u2103");
                                    weather.setDay("");
                                    weather.setWeatherIcon(mainIcon[sky_cover]);
                                    weathers.add(weather);
//
//                                    weather = new Weather();
//                                    weather.setBgColor(getResources().getColor(R.color.forestSunny,null));
//                                    weather.setTemperature(temp_max+"\u2103");
//                                    weather.setDay(temp_min+"\u2103");
//                                    weather.setWeatherIcon(weatherIcon[0]);
//                                    weathers.add(weather);
                                }
                                else {



                                    weather.setBgColor(getResources().getColor(R.color.forestSunny,
                                            null));
                                    weather.setPosition(i);
                                    weather.setTemperature(temp+"\u2103");
                                    weather.setDay(dayOfTheWeekName[nextDay -1]);
                                    weather.setWeatherIcon(weatherIcon[0]);
                                    weathers.add(weather);

                                }

                                // Minor work

                            }


                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapter = new Adapter(getApplicationContext(),weathers);
                            recyclerView.setAdapter(adapter);


//

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.d("tag", e.getMessage());
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("error: ", error.getMessage().toString());

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy( 5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
        requestQueue.getCache().clear();

    }
}
