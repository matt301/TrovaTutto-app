package com.example.matteo.trovatutto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Address> address_geo;
    private AlertDialog alert;


    class MyInfoWindowAdapter implements InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }
        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.info_title));
            tvTitle.setText(getIntent().getStringArrayListExtra("INFO").get(0).toString());
            TextView tvSubtitle = ((TextView)myContentsView.findViewById(R.id.info_subtitle));
            tvSubtitle.setText(getIntent().getStringArrayListExtra("INFO").get(1).toString());
            TextView tvDescription = ((TextView)myContentsView.findViewById(R.id.info_description));
            tvDescription.setText(getIntent().getStringArrayListExtra("INFO").get(2).toString());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {

            return null;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
             address_geo = geoCoder.getFromLocationName(getIntent().getExtras().getString("ADDRESS") , 1);

        } catch (IOException e) {

            e.printStackTrace();
        }

        if(address_geo.size() != 0) {
            // Add a marker in Genova and move the camera
            LatLng sydney = new LatLng(address_geo.get(0).getLatitude(), address_geo.get(0).getLongitude());
           Marker marker =  mMap.addMarker(new MarkerOptions().position(sydney).title("Ehi ciao!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.setMinZoomPreference(16);
            mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        }else {

            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

            builder.setTitle("Wrong Address").setMessage("The address isn't correct!!");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert = builder.create();
            alert.show();
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    MapsActivity.super.onBackPressed();
                }
            });
        }
    }


}
