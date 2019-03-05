package com.parking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment
        implements OnMapReadyCallback {

    private static final int REQUEST_CODE = 123;
    private GoogleMap mGoogleMap;
    private MapView mapView;
    private View mView;

    private static final LatLng GUATEMALA = new LatLng(14.594830, -90.483148);
    private LatLngBounds GUATEMALA_V2 = new LatLngBounds(
            new LatLng(14.594830, -90.483148), new LatLng(14.594830, -90.483148));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (getActivity() != null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle bundleConfig) {
        super.onViewCreated(view, bundleConfig);

        mapView = (MapView) mView.findViewById(R.id.map);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    private static final LatLng SYDNEY = new LatLng(-33.88, 151.21);
    private static final LatLng MOUNTAIN_VIEW = new LatLng(37.4, -122.1);


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        //googleMap.setMapType(GoogleMap.);

        googleMap.addMarker(new MarkerOptions().position(GUATEMALA));

        CameraPosition l = CameraPosition.builder()
                .target(GUATEMALA)
                .zoom(16)
                .bearing(0)
                .tilt(46)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(l));

    }


    /*private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }*/
/*
    @Override
    public void onMapLoaded() {

        LatLng customMarkerLocationOne = new LatLng(28.583911, 77.319116);
        LatLng customMarkerLocationTwo = new LatLng(28.583078, 77.313744);
        LatLng customMarkerLocationThree = new LatLng(28.580903, 77.317408);
        LatLng customMarkerLocationFour = new LatLng(28.580108, 77.315271);

        //LatLngBound will cover all your marker on Google Maps
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(customMarkerLocationOne); //Taking Point A (First LatLng)
        builder.include(customMarkerLocationThree); //Taking Point B (Second LatLng)
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);

        mMap.moveCamera(cu);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }
}*/
}

