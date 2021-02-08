package com.example.vchichvarin.weatherforecast;

import android.content.Context;
import android.location.LocationManager;

import com.example.vchichvarin.weatherforecast.data.WeatherData;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static androidx.core.content.ContextCompat.getSystemService;

public interface Api {
    static String DOMAIN = "http://api.openweathermap.org/";

    @GET("data/2.5/weather")

    Observable<WeatherData> getWeatherDataByCity(@Query("q") String city, @Query("appid") String appid, @Query("units") String units);

    class Instance {

        private static Retrofit getRetrofit() {

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            retrofitBuilder.baseUrl(DOMAIN);
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
            retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

            retrofitBuilder.client(okHttpClientBuilder.build());

            return retrofitBuilder.build();
        }

        public static Api getApi() {
            return getRetrofit().create(Api.class);
        }
    }

}
