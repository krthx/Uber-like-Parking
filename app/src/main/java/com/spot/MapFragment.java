package com.spot;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapFragment extends Fragment
        implements OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();

    private static final int REQUEST_CODE = 123;
    private GoogleMap mGoogleMap;
    private MapView mapView;
    private View mView;
    static Activity parent;


    public static  MapFragment newInstance(Activity a) {
        parent = a;

        return new MapFragment();
    }

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

        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        //googleMap.addMarker(new MarkerOptions().position(GUATEMALA));

        CameraPosition l = CameraPosition.builder()
                .target(GUATEMALA)
                .zoom(16)
                .bearing(0)
                .tilt(46)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(l));

        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                LatLng customMarkerLocationOne = new LatLng(28.583911, 77.319116);
                LatLng customMarkerLocationTwo = new LatLng(28.583078, 77.313744);
                LatLng customMarkerLocationThree = new LatLng(28.580903, 77.317408);
                LatLng customMarkerLocationFour = new LatLng(28.580108, 77.315271);
                mGoogleMap.addMarker(new MarkerOptions().position(customMarkerLocationOne).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(getActivity(), R.drawable.mandrish,"Manish")))).setTitle("iPragmatech Solutions Pvt Lmt");
                mGoogleMap.addMarker(new MarkerOptions().position(customMarkerLocationTwo).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(getActivity(), R.drawable.man,"Narender")))).setTitle("Hotel Nirulas Noida");

                mGoogleMap.addMarker(new MarkerOptions().position(customMarkerLocationThree).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(getActivity(), R.drawable.girl,"Neha")))).setTitle("Acha Khao Acha Khilao");
                mGoogleMap.addMarker(new MarkerOptions().position(customMarkerLocationFour).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(getActivity(), R.drawable.girl_two,"Nupur")))).setTitle("Subway Sector 16 Noida");

                //LatLngBound will cover all your marker on Google Maps
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(customMarkerLocationOne); //Taking Point A (First LatLng)
                builder.include(customMarkerLocationThree); //Taking Point B (Second LatLng)
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                mGoogleMap.moveCamera(cu);
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            }
        });
    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView)marker.findViewById(R.id.name);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
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

