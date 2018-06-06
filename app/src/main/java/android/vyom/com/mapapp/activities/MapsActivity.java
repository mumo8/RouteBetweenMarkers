package android.vyom.com.mapapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.vyom.com.mapapp.R;
import android.vyom.com.mapapp.model.DistanceMatrixModel;
import android.vyom.com.mapapp.network.ApiService;
import android.vyom.com.mapapp.network.RetrofitInstance;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final int REQUEST_CODE = 007;
    public static final double DEST_LAT = 38.788544;
    public static final double DEST_LNG = -90.493681;
    public static final String API_KEY = "AIzaSyDydgW8PC0QyuFr7UEnk3_MjGh5yyIHb1Y";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private Double myLat, myLng;
    private Double destLat, destLng;
    private Location currentLocation;
    private static final String TAG = "MapsActivity";
    private SupportMapFragment mapFragment;
    private ApiService apiService;
    private TextView tvDistance, tvTime;
    private LinearLayout dataContainer;
    // url: https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=41.587753,-87.935380&destinations=41.618560,-87.590684&key=AIzaSyDydgW8PC0QyuFr7UEnk3_MjGh5yyIHb1Y

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        tvDistance = findViewById(R.id.tvDistance);
        tvTime = findViewById(R.id.tvTime);
        dataContainer = findViewById(R.id.dataContainer);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        fusedLocationProviderApi = LocationServices.FusedLocationApi;

        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(4000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        apiService = RetrofitInstance.getInstance().create(ApiService.class);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng currentLocation = new LatLng(myLat, myLng);
//        if(destLat==null || destLng==null){
//            destLat = DEST_LAT;
//            destLng = DEST_LNG;
//        }

        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));

        if(destLat!=null && destLng!=null) {

            LatLng destination = new LatLng(destLat, destLng);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(currentLocation)
                    .include(destination).build();
            DrawRouteMaps.getInstance(this)
                    .draw(currentLocation, destination, mMap);
            mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));
        }

        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        myLocationUpdate();
    }

    private void myLocationUpdate() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;

        } else {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, MapsActivity.this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        currentLocation = location;
        myLat = currentLocation.getLatitude();
        myLng = currentLocation.getLongitude();
        Log.i(TAG, "onLocationChanged: lat: " + myLat + " lng: " + myLng);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this,data);
                destLat = place.getLatLng().latitude;
                destLng = place.getLatLng().longitude;

                apiService.getElements("imperial",myLat+","+myLng,destLat+","+destLng, API_KEY)
                        .enqueue(new Callback<DistanceMatrixModel>() {
                    @Override
                    public void onResponse(Call<DistanceMatrixModel> call, Response<DistanceMatrixModel> response) {
                        Log.i(TAG, "onResponse: "+response.body().getRows().get(0).getElements().get(0).getDistance().getText());
                        if(response.isSuccessful()){
                            dataContainer.setVisibility(View.VISIBLE);
                            tvDistance.setText("Distance: "+response.body().getRows().get(0).getElements().get(0).getDistance().getText());
                            tvTime.setText("Time: "+response.body().getRows().get(0).getElements().get(0).getDuration().getText());
                        }

                    }

                    @Override
                    public void onFailure(Call<DistanceMatrixModel> call, Throwable t) {

                    }
                });
            }
        }
    }

    public void startPlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(MapsActivity.this), REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
