package com.spot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spot.layout.MapWrapperLayout;
import com.spot.listeners.OnInfoWindowElemTouchListener;
import com.spot.models.Parking;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapFragment extends Fragment
        implements OnMapReadyCallback {

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);


        Button openButton = mView.findViewById(R.id.btnSearch);
        System.out.println("OpenButton" +openButton.getId());
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(view.getId());
                openAutocompleteActivity();
            }
        });

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!Places.isInitialized())
            Places.initialize(getContext(), getResources().getString(R.string.GOOGLE_MAPS_API_KEY));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle bundleConfig) {
        super.onViewCreated(view, bundleConfig);

        mapView =  mView.findViewById(R.id.map);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;

    private DatabaseReference mDatabase;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;

        //googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));

            if (!success) {
            }
        } catch (Resources.NotFoundException e) {
        }

        CameraPosition l = CameraPosition.builder()
                .target(GUATEMALA)
                .zoom(16)
                .bearing(0)
                .tilt(46)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(l));





        final MapWrapperLayout mapWrapperLayout = mView.findViewById(R.id.map_relative_layout);
        mapWrapperLayout.init(mGoogleMap, getPixelsFromDp(getContext(), 39 + 20));


        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window, null);
        this.infoTitle = infoWindow.findViewById(R.id.title);
        this.infoSnippet = infoWindow.findViewById(R.id.snippet);
        this.infoButton = infoWindow.findViewById(R.id.button);

        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
                getResources().getDrawable(R.drawable.fail),
                getResources().getDrawable(R.drawable.history))
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                Parking p = (Parking) marker.getTag();

                Toast.makeText(getContext(), p.uid, Toast.LENGTH_SHORT).show();

                Intent prof = new Intent(getContext(), ProfileActivity.class);
                prof.putExtra("id-profile", p.uid);

                startActivity(prof);
            }
        };

        this.infoButton.setOnTouchListener(infoButtonListener);


        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Parking p = (Parking) marker.getTag();

                infoTitle.setText(marker.getTitle());
                infoSnippet.setText("Q." + p.costPerHour + " /por hora");
                infoButtonListener.setMarker(marker);

                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);

                return infoWindow;
            }
        });







        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {


                mDatabase = FirebaseDatabase.getInstance().getReference("parking/parking-profiles//" );


                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        Parking newParking = dataSnapshot.getValue(Parking.class);

                        ///adapter.addCard(newCard);
                        LatLng customMarkerLocationOne = new LatLng(newParking.longitude, newParking.latitude);

                        System.out.println("Parking: " + newParking.owner);
                        MarkerOptions opt = new MarkerOptions()
                                .position(customMarkerLocationOne)
                                .snippet(newParking.uid)
                                .title(newParking.title)
                                .icon(BitmapDescriptorFactory.fromBitmap(
                                        createCustomMarker(getActivity(), R.drawable.profile_parking, newParking.owner)
                                ));

                        Marker m = mGoogleMap.addMarker(opt);
                        m.setTag(newParking);

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(GUATEMALA); //Taking Point A (First LatLng)
                        builder.include(GUATEMALA); //Taking Point B (Second LatLng)
                        LatLngBounds bounds = builder.build();
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                        mGoogleMap.moveCamera(cu);
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

                /*LatLng customMarkerLocationOne = new LatLng(14.598848, -90.486804);
                LatLng customMarkerLocationTwo = new LatLng(14.598521, -90.484368);
                LatLng customMarkerLocationThree = new LatLng(14.599284, -90.486058);
                LatLng customMarkerLocationFour = new LatLng(14.600447, -90.486219);*/
                /*mGoogleMap.addMarker(new MarkerOptions()
                        .position(customMarkerLocationOne)
                        .icon(
                                BitmapDescriptorFactory
                                        .fromBitmap(
                                                createCustomMarker(getActivity(), R.drawable.profile_parking,"Juan")
                                        )
                        )).setTitle("Parqueo 1");*/

                /*MarkerOptions opt = new MarkerOptions()
                        .position(customMarkerLocationTwo).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(getActivity(), R.drawable.profile_parking,"Ramón"))
                        )
                        .snippet("Id")
                        .title("Parqueo 2");


                Parking p = new Parking();
                p.uid = "ASDASDF";

                Marker m = mGoogleMap.addMarker(opt);
                m.setTag(p);
*/
                /*mGoogleMap.addMarker(new MarkerOptions().position(customMarkerLocationThree).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(getActivity(), R.drawable.profile_parking,"María")))).setTitle("Parqueo 3");
                mGoogleMap.addMarker(new MarkerOptions().position(customMarkerLocationFour).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(getActivity(), R.drawable.profile_parking,"Marcela")))).setTitle("Parqueo 4");*/

                //LatLngBound will cover all your marker_blue on Google Maps

            }
        });
    }



    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
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


    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    public static int REQUEST_CODE_AUTOCOMPLETE = 778;

    private void openAutocompleteActivity() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            System.out.println("Error " + e.getMessage());
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(), 0 ).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            System.out.println("Error " + e.getMessage());

            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            System.out.println("ResultCode " + resultCode);


            try {

                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getContext(), data);

                /*mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));*/

                System.out.println(place.getName() + " " + place.getId() + " " +  place.getAddress() + " " +  place.getPhoneNumber() + " " +
                        place.getWebsiteUri());

                /*CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mPlaceAttribution.setText("");
                }*/
            }catch (Exception e) {}
            /*if (resultCode == 2) {

                try {

                    // Get the user's selected place from the Intent.
                    Place place = PlaceAutocomplete.getPlace(getContext(), data);

                *//*mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));*//*

                    System.out.println(place.getName() + " " + place.getId() + " " +  place.getAddress() + " " +  place.getPhoneNumber() + " " +
                            place.getWebsiteUri());

                *//*CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mPlaceAttribution.setText("");
                }*//*
                }catch (Exception e) {}
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
            } else if (resultCode == 1) {
            }*/
        }
    }
}