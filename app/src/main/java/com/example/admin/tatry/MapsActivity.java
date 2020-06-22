package com.example.admin.tatry;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.lang.String;

public class MapsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    private MapView mapView;
    MapboxMap map;
    boolean on = false;
    LocationServices locationServices;
    FloatingActionButton floatingActionButton;
    static final int PERMISSIONS_LOCATION = 0;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        MapboxAccountManager.start(this, getString(R.string.access_token));
        setContentView(R.layout.activity_maps);
        locationServices = LocationServices.getLocationServices(MapsActivity.this);
        mapView = (com.mapbox.mapboxsdk.maps.MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
            }
        });

        floatingActionButton = (FloatingActionButton) findViewById(R.id.location_toggle_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (map != null) {
                    on = true;
                    toggleGps(!map.isMyLocationEnabled());
                }
            }
        });
        mapView.invalidate();
    }

    public void Menu(View v)
    {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.main, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.option1:
                Intent intent = new Intent(this, LengthActivity.class);
                startActivity(intent);
                return true;
            case R.id.option2:
                Intent intent4 = new Intent(this, HostelsActivity.class);
                startActivity(intent4);
                return true;
            default:
                return false;
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void toggleGps(boolean enableGps)
    {
        if (enableGps)
        {
            // Check if user has granted location permission
            if (!locationServices.areLocationPermissionsGranted())
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
            }
            else
                enableLocation(true);
        }
        else
            enableLocation(false);
    }

    private void enableLocation(boolean enabled)
    {
        if (enabled)
        {
            // If we have the last location of the user, we can move the camera to that position.
            Location lastLocation = locationServices.getLastLocation();
            if (lastLocation != null)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));

            locationServices.addLocationListener(new LocationListener()
            {
                @Override
                public void onLocationChanged(Location location)
                {
                    if (location != null)
                    {
                        // Move the map camera to where the user location is and then remove the
                        // listener so the camera isn't constantly updating when the user location
                        // changes. When the user disables and then enables the location again, this
                        // listener is registered again and will adjust the camera once again.
                        latLng = new LatLng(location);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        mapView.invalidate();
                        locationServices.removeLocationListener(this);
                    }
                }
            });
            floatingActionButton.setImageResource(R.drawable.ic_location_disabled_24dp);
        }
        else
            floatingActionButton.setImageResource(R.drawable.ic_my_location_24dp);

        // Enable or disable the location layer on the map
        map.setMyLocationEnabled(enabled);
    }

}
