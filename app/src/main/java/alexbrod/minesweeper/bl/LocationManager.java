package alexbrod.minesweeper.bl;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;


/**
 * Created by Alex Brod on 1/18/2017.
 */

public class LocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>,
        LocationListener {

    private static final long LOCATION_REQUEST_INTERVAL = 10000;
    private static final long LOCATION_REQUEST_FASTEST_INTERVAL = 5000;

    private static LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    private LocationSettingsRequest.Builder locationSettingsRequestBuilder;
    private LocationRequest locationRequest;
    private Location updatedLocation;
    private Activity parentActivity;
    private boolean isLocationResolutionRequestTried;

    private LocationManager(Activity activity) {
        this.parentActivity = activity;
        isLocationResolutionRequestTried = false;
        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(activity.getBaseContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    public static LocationManager getInstance(Activity activity) {
        if (locationManager == null) {
            locationManager = new LocationManager(activity);
        }
        return locationManager;
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
    }

    public void connect() {
        googleApiClient.connect();
    }

    public void disconnect() {
        googleApiClient.disconnect();
        isLocationResolutionRequestTried = false;
    }

    public boolean isConnected(){
        return googleApiClient.isConnected();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(googleApiClient.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(googleApiClient.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, this);
    }


    public void stopLocationUpdates() {
        if(isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
    }

    public double getUpdatedLongitude(){
        return updatedLocation.getLongitude();
    }

    public double getUpdatedLatitude(){
        return updatedLocation.getLatitude();
    }

    public boolean isLocationUpdated(){
        if(updatedLocation == null){
            return false;
        }
        return true;
    }

    public void tryStartLocationUpdates() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
                        locationSettingsRequestBuilder.build());
        result.setResultCallback(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();
        tryStartLocationUpdates();
    }



    @Override
    public void onConnectionSuspended(int i) {
        updatedLocation = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        updatedLocation = null;
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        //        final LocationSettingsStates = result.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // All location settings are satisfied. The client can
                // initialize location requests here.
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                if(isLocationResolutionRequestTried){
                    break;
                }
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().

                    status.startResolutionForResult(
                            parentActivity,
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
                    isLocationResolutionRequestTried = true;


                } catch (SendIntentException e) {
                    Log.e(this.getClass().getSimpleName(),"Location resolution error");
                }
                break;
            case LocationSettingsStatusCodes.NETWORK_ERROR:
                Log.e(this.getClass().getSimpleName(),"Network Error");
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are not satisfied. However, we have no way
                // to fix the settings so we won't show the dialog.
                Log.e(this.getClass().getSimpleName(),"Unknown Location Error");
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updatedLocation = location;
    }

}
