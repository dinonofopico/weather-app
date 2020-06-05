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
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date = new Date();

        recyclerView = findViewById(R.id.weather_lists);
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        currentTemp1 = (TextView) findViewById(R.id.currentTemp1);
        minTemp = (TextView) findViewById(R.id.minTemp);
        maxTemp = (TextView) findViewById(R.id.maxTemp);
        currentTempImg = (ImageView) findViewById(R.id.currentTempImg);
        weathers = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();

    }


    private void checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

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

                        Toast.makeText(getApplicationContext(),
                                addresses.get(0).getAddressLine(0)
                                , Toast.LENGTH_LONG).show();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                }

            }
        });

    }

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


                            for(int i = 0; i < 6; i++ ){

                                weather = new Weather();

                                JSONObject tempDetails = null, sky = null;
                                JSONArray  weatherDetails = null;

                                JSONObject tempObj = weatherData.getJSONObject(i);

                                tempDetails = tempObj.getJSONObject("main");

                                weatherDetails = tempObj.getJSONArray("weather");

                                sky = weatherDetails.getJSONObject(0);

                                String condition = sky.getString("main");



                                String temp = tempDetails.getString("temp"),
                                temp_min = tempDetails.getString("temp_min"),
                                        temp_max = tempDetails.getString("temp_max");


                                if(i > 0){

                                    weather.setTemperature(temp+"\u2103");
                                    weather.setDay(condition);
                                    weathers.add(weather);

                                }


                                if(i == 0){
//
                                    currentTemp.setText(temp+"\u2103"+"\n"+condition);
                                    currentTemp1.setText(temp+"\u2103"+"\nclouds");
                                    minTemp.setText(temp_min+"\u2103");
                                    maxTemp.setText(temp_max+"\u2103");

                                    if(condition == "Cloudy")
                                        currentTempImg.setImageResource(R.drawable.forest_cloudy);

                                    if(condition == "Rain")
                                        currentTempImg.setImageResource(R.drawable.forest_rainy);

                                    if(condition.equals("Clear")){

                                        currentTempImg.setImageResource(R.drawable.forest_sunny);
//                                        currentTemp.setBackgroundColor(getResources().
//                                                getColor(R.color.forestSunny, null));

                                    }

                                }
//
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
