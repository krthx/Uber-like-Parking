package com.spot;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.spot.custom.BookingDialog;
import com.spot.custom.CreditCardDialog;
import com.spot.custom.MultiSpinner;
import com.spot.models.Parking;
import com.spot.models.Reserva;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    Parking parkingProfile;
    TextView txtTitle;
    TextView txtOwner;
    TextView txtCost;
    TextView txtPhone;
    Button btnReservar;
    ImageView btnCall;
    ImageView btnWp;
    Button btnBack;
    List<Reserva> reservedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        final String profile = intent.getStringExtra("id-profile");

        mDatabase = FirebaseDatabase.getInstance().getReference("parking/parking-profiles/" + profile );
        reservedList = new ArrayList<>();

        System.out.println("Id: " + profile);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                final int[] schs = { R.id.sch1, R.id.sch2, R.id.sch3, R.id.sch4, R.id.sch5, R.id.sch6, R.id.sch7, R.id.sch8, R.id.sch9, R.id.sch10,
                        R.id.sch11, R.id.sch12, R.id.sch13, R.id.sch14, R.id.sch15, R.id.sch16, R.id.sch17, R.id.sch18, R.id.sch19, R.id.sch20, R.id.sch21,
                        R.id.sch22, R.id.sch23, R.id.sch24 };
                final Parking newParking = dataSnapshot.getValue(Parking.class);

                parkingProfile = newParking;

                txtTitle.setText(newParking.title);
                txtOwner.setText(newParking.owner);
                txtPhone.setText(newParking.phone);
                txtCost.setText("Q. " + newParking.costPerHour + " / por hora");

                final int stA = Integer.parseInt(newParking.availability.split("-")[0].split(":")[0].trim());
                final int edA = Integer.parseInt(newParking.availability.split("-")[1].split(":")[0].trim());


                for(int i = 0; i < schs.length; i++) {
                    if(!(stA <= i && i < edA)){
                        TextView t = findViewById(schs[i]);
                        t.setText(R.string.no_available);
                        t.setTextColor(Color.DKGRAY);
                    }
                }


                btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            String[] PERMISSIONS = { Manifest.permission.CALL_PHONE };
                            if (!hasPermissions(ProfileActivity.this, PERMISSIONS)) {
                                ActivityCompat.requestPermissions((Activity) ProfileActivity.this, PERMISSIONS, REQUEST );
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + newParking.phone));
                                startActivity(intent);
                            }
                        } else {
                        }
                    }
                });

                btnWp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://api.whatsapp.com/send?phone=+502 " + newParking.phone;

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));

                        startActivity(i);
                    }
                });

                btnReservar = findViewById(R.id.btnReservar);
                btnReservar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BookingDialog dialog = new BookingDialog(ProfileActivity.this, newParking.availability, reservedList, parkingProfile.uid);

                        dialog.show();
                    }
                });

                try{
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                    final Calendar now = Calendar.getInstance();
                    now.set(Calendar.HOUR, 0);
                    now.set(Calendar.MINUTE, 0);
                    now.set(Calendar.SECOND, 0);
                    now.set(Calendar.MILLISECOND, 0);

                    Query query = reference.child("booking").orderByChild("parking").equalTo(profile);

                    System.out.println("Time to consulting: " + now.getTime().getTime());
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                            final Reserva newBooking = dataSnapshot.getValue(Reserva.class);

                            if(newBooking.day == now.getTimeInMillis()) {
                                reservedList.add(newBooking);

                                try{
                                    String startHour = newBooking.startHour.split(":")[0];
                                    String endHour = newBooking.endHour.split(":")[0];

                                    for(int i = 0; i < schs.length; i++) {
                                        int st = Integer.parseInt(startHour);
                                        int ed = Integer.parseInt(endHour);

                                        if(stA <= i && i < edA){
                                            if( st <= i && i < ed) {
                                                TextView t = findViewById(schs[i]);
                                                t.setText(R.string.busy);
                                                t.setTextColor(Color.RED);
                                            }
                                        }
                                    }

                                }catch ( Exception e ) {
                                    e.printStackTrace();
                                }
                            }
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
                }catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtTitle = findViewById(R.id.txtTitle);
        txtOwner = findViewById(R.id.txtOwner);
        txtCost = findViewById(R.id.txtCost);
        txtPhone = findViewById(R.id.txtPhone);
        btnReservar = findViewById(R.id.btnReservar);

        btnCall = findViewById(R.id.btnCall);
        btnWp = findViewById(R.id.btnWP);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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


    private static final int REQUEST = 114;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //call get location here
                } else {
                    Toast.makeText(this, "The app was not allowed to access your location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
    SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


}
