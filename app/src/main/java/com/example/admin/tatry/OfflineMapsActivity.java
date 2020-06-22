package com.example.admin.tatry;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.TextView;

import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.util.ArrayList;

public class OfflineMapsActivity extends Activity implements LocationListener {

    private MapView mapView;
    private IMapController mapController;
    GeoPoint punkt;
    GeoPoint endPoint;
    GeoPoint updatePoint;
    private double Latitude;
    private double Longitude;
    Context context;

    LocationManager locationManager;
    String bestDostawca;
    Criteria criteria;
    Location location;
    CompassOverlay compassOverlay;
    Marker startMarker;
    BoundingBox boundingBox;

    TextView t1;
    TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_maps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Latitude = Coordinates.getLatitude();
        Longitude = Coordinates.getLongitude();

        t1 = (TextView) findViewById(R.id.wspolrzedne1);
        t2 = (TextView) findViewById(R.id.wspolrzedne2);

        context = OfflineMapsActivity.this;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        mapView = (org.osmdroid.views.MapView) findViewById(R.id.mapview_offline);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setTileSource(new XYTileSource("Mapnik", 6, 17, 256, ".png", new String[]{}));

        mapView.setUseDataConnection(false);
        mapView.setBuiltInZoomControls(true);
        mapView.setClickable(true);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(10);

        if (mapView.getZoomLevel() >= 6 && mapView.getZoomLevel() <= 10)
            boundingBox = new BoundingBox(55.1339, 25.2548, 48.0228, 13.0567);
        else
            boundingBox = new BoundingBox(55.1339, 25.2548, 48.0228, 13.0567);

        Schr(Latitude, Longitude);

        criteria = new Criteria();
        //pobranie pozycji GPS

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //pobranie pozycji GPS
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        refresh();
        locationManager.requestLocationUpdates(bestDostawca, 1000, 1, this); //(, czas odświeżania w ms, co jaki dystans w m,)
        if(location!=null)
            punkt = new GeoPoint(location.getLatitude(), location.getLongitude()); //nasze położenie

        // kompas
        this.compassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), mapView);
        this.compassOverlay.enableCompass();
        mapView.getOverlays().add(this.compassOverlay);

        if(location!=null) {
            //nawigacja
            startMarker = new Marker(mapView);
            startMarker.setPosition(punkt);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(startMarker);

            RoadManager roadManager = new MapQuestRoadManager("gtWwqEDPViqASK1kqG7X5lcvZnRT9Fj7");
            roadManager.addRequestOption("routeType=bicycle");

            ArrayList<GeoPoint> waypoints = new ArrayList<>();
            waypoints.add(punkt);
            endPoint = new GeoPoint(Latitude, Longitude);
            waypoints.add(endPoint);

            Road road = roadManager.getRoad(waypoints);

            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

            for (int i = 0; i < road.mNodes.size(); i++) {
                RoadNode node = road.mNodes.get(i);
                Marker nodeMarker = new Marker(mapView);
                nodeMarker.setPosition(node.mLocation);
            }
            if (getDistanceInMeters(punkt, endPoint) >= 1000)
                t1.setText("Dystans " + Float.toString(getDistanceInMeters(punkt, endPoint) / 1000) + "km");
            else
                t1.setText("Dystans " + Float.toString(getDistanceInMeters(punkt, endPoint)) + "m");

            mapView.getOverlays().add(roadOverlay);

            mapView.setMapListener(new MapListener() {
                public boolean onZoom(ZoomEvent arg0) {
                    // TODO Auto-generated method stub
                    if (mapView.getZoomLevel() == 11) {
                        boundingBox = new BoundingBox(49.3380, 20.2316, 49.1141, 19.7340);
                        mapController.setZoom(14);
                        GeoPoint pkt = new GeoPoint(Latitude, Longitude);
                        mapController.setCenter(pkt);
                    } else if (mapView.getZoomLevel() == 13) {
                        boundingBox = new BoundingBox(55.1339, 25.2548, 48.0228, 13.0567);
                        mapController.setZoom(10);
                        mapController.setCenter(punkt);
                    }
                    return false;
                }

                public boolean onScroll(ScrollEvent arg0) {
                    // TODO Auto-generated method stub
                    mapView.setScrollableAreaLimitDouble(boundingBox);
                    return false;
                }
            });
        }
        else
        {
            mapView.setMapListener(new MapListener() {
                public boolean onZoom(ZoomEvent arg0) {
                    // TODO Auto-generated method stub
                    if (mapView.getZoomLevel() == 11) {
                        boundingBox = new BoundingBox(49.3380, 20.2316, 49.1141, 19.7340);
                        mapController.setZoom(14);
                        GeoPoint pkt = new GeoPoint(Latitude, Longitude);
                        mapController.setCenter(pkt);
                    } else if (mapView.getZoomLevel() == 13) {
                        boundingBox = new BoundingBox(55.1339, 25.2548, 48.0228, 13.0567);
                        mapController.setZoom(10);
                        punkt = new GeoPoint(49.2814, 19.9611);
                        mapController.setCenter(punkt);
                    }
                    return false;
                }

                public boolean onScroll(ScrollEvent arg0) {
                    // TODO Auto-generated method stub
                    mapView.setScrollableAreaLimitDouble(boundingBox);
                    return false;
                }
            });
            buildAlertMessageErrorGPS();
        }
        mapView.invalidate();
    }


    public void Schr(double Latitude, double Longitude) {
        GeoPoint startPoint = new GeoPoint(Latitude, Longitude);
        mapController.setCenter(startPoint);
    }

    private void refresh() {
        bestDostawca = locationManager.getBestProvider(criteria, true); //GPS oraz sieć komórkowa
        //bestDostawca = locationManager.NETWORK_PROVIDER; //tylko sieć komórkowa
        //bestDostawca = locationManager.GPS_PROVIDER;    //tylko GPS

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        location = locationManager.getLastKnownLocation(bestDostawca);
    }

    public float getDistanceInMeters(GeoPoint p1, GeoPoint p2) {
        double lat1 = ((double) p1.getLatitudeE6()) / 1e6;
        double lng1 = ((double) p1.getLongitudeE6()) / 1e6;
        double lat2 = ((double) p2.getLatitudeE6()) / 1e6;
        double lng2 = ((double) p2.getLongitudeE6()) / 1e6;
        float[] dist = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, dist);
        return dist[0];
    }

    public void onLocationChanged(Location location) {
        refresh();
        if(location != null) {
            if(startMarker != null){
                updatePoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                startMarker.setPosition(updatePoint);
            }
            if(getDistanceInMeters(punkt, endPoint) > 1000)
                t1.setText("Dystans " + Float.toString(getDistanceInMeters(punkt, endPoint)/1000) + "km");
            else
                t1.setText("Dystans " + Float.toString(getDistanceInMeters(punkt, endPoint)) + "m");
            mapController.setCenter(updatePoint);
        }
    }

    private void buildAlertMessageErrorGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Nadajnik GPS uległ awarii. Chcesz zmienić tryb lokalizacji?")
                .setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public void onProviderDisabled(String provider){
        // TODO: Auto-generated method
    }
    public void onProviderEnabled(String provider){
        // TODO: Auto-generated method
    }
    public void onStatusChanged(String provider, int status, Bundle extras){
        // TODO: Auto-generated method
    }
}