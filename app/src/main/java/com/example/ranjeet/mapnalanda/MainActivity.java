package com.example.ranjeet.mapnalanda;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private  SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    private  static final int REQUEST_ACCESS_FINE_LOCATION=99;
    Button btn_googleMap;
    Button btn_college;

    GoogleMap mgoogleMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap=googleMap;
        checkPermission();
        nalandaOnMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.mapTypeNormal: mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeTerrain: mgoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeSatellite: mgoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeHybrid: mgoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    //user define methods
    private void checkPermission(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Loacation Permission Needed..");
                builder.setMessage("This app need to access your device Location...Please Allow");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ACCESS_FINE_LOCATION);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            } else if(sharedPreferences.getBoolean("RequestedLocation",true)){
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ACCESS_FINE_LOCATION);
                editor.putBoolean("RequestedLocation",false);
                editor.commit();
                // REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            else
            {
                Toast.makeText(this,"Please allow location permission in your app Settings",Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri=Uri.fromParts("package",this.getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }
        } else {
            // Permission has already been granted
            // for Map tool disable
            mgoogleMap.getUiSettings().setMapToolbarEnabled(true);;
            mgoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mgoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mgoogleMap.getUiSettings().setCompassEnabled(true);
            mgoogleMap.setMyLocationEnabled(true);
        }

    }

    //go to specific location on Map with Zoom
    private void goToLocationZoom(double lat,double log,float zoom){
        LatLng latLng=new LatLng(lat,log);
        CameraUpdate update=CameraUpdateFactory.newLatLngZoom(latLng,zoom);
        mgoogleMap.moveCamera(update);
    }
    //Go  own College Nalanda on Starting
    private void nalandaOnMap(){
      double nalLat=20.365743;
      double nalLog=85.760715;
      float zoom=15;
      LatLng latLng=new LatLng(nalLat,nalLog);
      mgoogleMap.addMarker(new MarkerOptions().title("Nalanda Institue Of Technology,Chandaka,Bhubneswar")
              .position(latLng));
      mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    public void googleMap(View view) {

        switch (view.getId()){
            case R.id.googleMap: Uri gmmIntentUri = Uri.parse("geo:20.365743,85.760715");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }else
                    Toast.makeText(this,"Can't Open Google Map",Toast.LENGTH_LONG).show();
                break;
            case R.id.goNalanda:
                nalandaOnMap();
                break;
        }

    }
}
