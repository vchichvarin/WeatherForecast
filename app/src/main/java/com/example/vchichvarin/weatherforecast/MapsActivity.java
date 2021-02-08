package com.example.vchichvarin.weatherforecast;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vchichvarin.weatherforecast.data.WeatherData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private Api mApi = Api.Instance.getApi();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button moscowButton = findViewById(R.id.moscow_button);
        moscowButton.setOnClickListener(this);

        findViewById(R.id.bishkek_button).setOnClickListener(this);
    }

    private void receiveWeather(final String city) {

        mApi.getWeatherDataByCity(city, "772b0b7acb797dfc8ed7a61a35d97ecf", "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherData>() {
                    @Override
                    public void accept(WeatherData weatherData) throws Exception {
                        Toast.makeText(MapsActivity.this, weatherData.getName() + " " + weatherData.getMain().getTemp(), Toast.LENGTH_LONG).show();
                        showWeatherMarker(weatherData);
                    }
                });
    }

    private void showWeatherMarker(WeatherData weatherData) {
        LatLng latLng = new LatLng(weatherData.getCoord().getLat(), weatherData.getCoord().getLon());
        mMap.addMarker(new MarkerOptions().position(latLng).title(weatherData.getName() + weatherData.getMain().getTemp())).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

    }

    @Override
    public void onClick(View v) {
        receiveWeather(((Button)v).getText().toString());
    }

}