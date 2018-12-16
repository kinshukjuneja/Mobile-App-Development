package edu.neu.madcourse.kinshukjuneja.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.kinshukjuneja.activity.horoscope.HoroscopeUserDetailsActivity;

public class LocationHelper {

    private static LocationHelper singletonRef;
    private HoroscopeUserDetailsActivity activity;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double lat;
    private double lon;
    private boolean locationFound;
    private boolean permissionDenied;

    private LocationHelper(Activity activity) {
        this.activity = (HoroscopeUserDetailsActivity) activity;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        initLocationListener();
    }

    public static LocationHelper getSingletonRef(Activity activity) {
        if(singletonRef == null) {
            singletonRef = new LocationHelper(activity);
        }
        return singletonRef;
    }

    public void initLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                activity.setCity(getCityName());
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }

        };
    }

    public void setupLocationManager(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        }
    }

    public void permissionsGranted(String[] permissions, int[] grantResults) {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                return;
            }
        }
        Toast.makeText(activity, "Location permission is not set. Skipping city auto detection.", Toast.LENGTH_LONG).show();
    }

    public String getCityName() {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() == 0 || addresses.get(0).getLocality() == null) {
            Toast.makeText(activity, "Could not detect city. Please enter manually.", Toast.LENGTH_LONG).show();
            return "";
        } else {
            Log.d("Location locality : ", addresses.get(0).getLocality());
            return addresses.get(0).getLocality();
        }
    }

}
