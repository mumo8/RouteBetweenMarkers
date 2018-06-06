package android.vyom.com.mapapp.network;

import android.vyom.com.mapapp.model.DistanceMatrixModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    // url: https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=41.587753,-87.935380&destinations=41.618560,-87.590684&key=AIzaSyDydgW8PC0QyuFr7UEnk3_MjGh5yyIHb1Y


    @GET("json")
    Call<DistanceMatrixModel> getElements(@Query("units") String units,
                                          @Query("origins") String origins,
                                          @Query("destinations") String destinations,
                                          @Query("key") String api_key);
}
