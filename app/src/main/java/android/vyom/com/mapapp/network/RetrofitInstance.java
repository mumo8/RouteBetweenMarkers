package android.vyom.com.mapapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;

    private static Gson gson = new GsonBuilder().setLenient().create();

    public static Retrofit getInstance(){
        if(retrofit == null){

            retrofit = new retrofit2.Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .baseUrl("https://maps.googleapis.com/maps/api/distancematrix/")
                        .build();

        }
        return retrofit;
    }
}
